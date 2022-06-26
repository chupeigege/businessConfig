package vip.aquan.businessconfig.service.impl;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vip.aquan.businessconfig.entity.ConfigValue;
import vip.aquan.businessconfig.service.ConfigValueService;
import vip.aquan.businessconfig.vo.ConfigValueReq;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Chupei Wang
 * since 2022/6/26 16:12
 */
@Service
public class ConfigValueServiceImpl implements ConfigValueService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigValueServiceImpl.class);
    private final MongoTemplate mongoTemplate;
    private final StringRedisTemplate redisTemplate;
    String prefix = "moh_rsc:business:config:";

    public ConfigValueServiceImpl(
            MongoTemplate mongoTemplate,
            StringRedisTemplate redisTemplate
    ) {
        this.mongoTemplate = mongoTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Page<ConfigValue> page(Pageable pageable, String key) {
        List<ConfigValue> result;
        long count;
        if (!StringUtils.isEmpty(key)) {
            Pattern pattern = Pattern.compile("^.*" + key + ".*$", Pattern.CASE_INSENSITIVE);
            final Query query = Query.query(Criteria.where("key").regex(pattern)).with(pageable);
            result = mongoTemplate.find(query, ConfigValue.class);
            count = mongoTemplate.count(Query.query(Criteria.where("key").regex(pattern)), ConfigValue.class);
        } else {
            final Query query = Query.query(Criteria.where("key").exists(true)).with(pageable);
            result = mongoTemplate.find(query, ConfigValue.class);
            count = mongoTemplate.count(Query.query(Criteria.where("key").exists(true)), ConfigValue.class);
        }
        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public ConfigValue add(ConfigValueReq vo) {
        logger.info("ConfigValueServiceImpl.add.vo:{}", JSON.toJSONString(vo));
        ConfigValue temp = mongoTemplate.findOne(Query.query(Criteria.where("key").is(vo.getId())), ConfigValue.class);
        if (temp != null) {
            return temp;
        } else {
            ConfigValue configValue = ConfigValue.builder()
                    .id(vo.getId())
                    .key(vo.getId())
                    .expires(vo.getExpires())
                    .value(vo.getValue())
                    .remark(vo.getRemark())
                    .updateTime(System.currentTimeMillis())
                    .createTime(System.currentTimeMillis())
                    .build();
            mongoTemplate.save(configValue);
            redisTemplate.delete(prefix + vo.getId());
            return configValue;
        }
    }

    @Override
    public String get(String key) {
        String value = redisTemplate.opsForValue().get(prefix + key);
        if (!StringUtils.isEmpty(value)) {
            return value;
        } else {
            logger.info("ConfigValueServiceImpl.get.cache not hit. key:{}", key);
            ConfigValue temp = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(key)), ConfigValue.class);
            if (temp != null) {
                redisTemplate.opsForValue().set(prefix + temp.getKey(), temp.getValue(), Duration.ofSeconds(temp.getExpires()));
                return temp.getValue();
            } else {
                throw  new RuntimeException("config value key not found");
            }
        }
    }

    @Override
    public boolean update(ConfigValueReq vo) {
        logger.info("ConfigValueServiceImpl.update.vo:{}", JSON.toJSONString(vo));
        Update update = new Update();
        if (!StringUtils.isEmpty(vo.getRemark())) {
            update.set("remark", vo.getRemark());
        }
        update.set("value", vo.getValue());
        update.set("expires", vo.getExpires());
        update.set("updateTime", System.currentTimeMillis());
        mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(vo.getId())), update, ConfigValue.class).getModifiedCount();
        redisTemplate.delete(prefix + vo.getId());
        return true;
    }

    @Override
    public boolean delete(String id) {
        logger.info("ConfigValueServiceImpl.delete.id:{}", id);
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(id)), ConfigValue.class);
        redisTemplate.delete(prefix + id);
        return true;
    }

    @Override
    public boolean deleteCache(String key) {
        logger.info("ConfigValueServiceImpl.deleteCache.key:{}", key);
        if (StringUtils.isEmpty(key)) {
            List<ConfigValue> all = mongoTemplate.findAll(ConfigValue.class);
            if (!all.isEmpty()) {
                redisTemplate.delete(all.stream().map(x -> prefix + x.getKey()).collect(Collectors.toList()));
            }
        } else {
            redisTemplate.delete(prefix + key);
        }
        return true;
    }
}

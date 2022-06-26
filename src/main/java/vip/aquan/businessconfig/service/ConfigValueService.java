package vip.aquan.businessconfig.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vip.aquan.businessconfig.entity.ConfigValue;
import vip.aquan.businessconfig.vo.ConfigValueReq;


/**
 * @author Chupei Wang
 * since 2022/6/26 16:12
 */
public interface ConfigValueService {


    Page<ConfigValue> page(Pageable pageable, String key);

    ConfigValue add(ConfigValueReq vo);

    String get(String key);

    boolean update(ConfigValueReq vo);

    boolean delete(String id);

    boolean deleteCache(String key);
}

package vip.aquan.businessconfig.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Chupei Wang
 * since 2022/6/26 16:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "business_config")
public class ConfigValue {

    private String id;

    private String key;

    private String value;

    private String remark;

    private Long expires;

    private Long createTime;

    private Long updateTime;

}

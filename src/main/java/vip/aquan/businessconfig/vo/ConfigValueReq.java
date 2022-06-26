package vip.aquan.businessconfig.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Chupei Wang
 * since 2022/6/26 16:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigValueReq {

    @NotNull(message = "id is required.")
    private String id;

    @NotNull(message = "value is required.")
    private String value;

    private String remark;

    // 缓存有效期 单位/秒，默认十分钟
    @NotNull(message = "expires time is required.")
    @Min(value = 1, message = "Field 'expires' value must be larger than 1")
    private Long expires = 600L;
}

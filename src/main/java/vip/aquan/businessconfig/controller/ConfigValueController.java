package vip.aquan.businessconfig.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import vip.aquan.businessconfig.entity.ConfigValue;
import vip.aquan.businessconfig.service.ConfigValueService;
import vip.aquan.businessconfig.util.PageUtils;
import vip.aquan.businessconfig.vo.ConfigValueReq;
import vip.aquan.businessconfig.vo.RestResponse;

import javax.validation.Valid;


/**
 * @author Chupei Wang
 * since 2022/6/26 16:12
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/config")
public class ConfigValueController {

    private final ConfigValueService configValueService;

    public ConfigValueController(ConfigValueService configValueService) {
        this.configValueService = configValueService;
    }


    @GetMapping(value = "/page")
    public RestResponse<Page<ConfigValue>> page(
            @RequestParam(required = false)
                    String key,
            @RequestParam(required = false, defaultValue = "1")
                    int pageNum,
            @RequestParam(required = false, defaultValue = "10")
                    int pageSize,
            @RequestParam(required = false, defaultValue = "createdTime")
                    String sortField,
            @RequestParam(required = false, defaultValue = "false")
                    Boolean isAsc
    ) {
        return new RestResponse<>("page successfully", configValueService.page(PageUtils.getPageable(pageNum, pageSize, Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortField)), key));
    }

    @GetMapping(value = "/get")
    public RestResponse<String> get(@RequestParam String key) {
        return new RestResponse<>("get successfully.value:" + configValueService.get(key));
    }

    @PostMapping(value = "/add")
    public RestResponse<ConfigValue> add(@RequestBody @Valid ConfigValueReq req
    ) {
        return new RestResponse<>("add successfully", configValueService.add(req));
    }

    @PostMapping(value = "/update")
    public RestResponse update(@RequestBody @Valid ConfigValueReq req
    ) {
        configValueService.update(req);
        return RestResponse.ok("update successfully");
    }

    @PostMapping(value = "/delete/{id}")
    public RestResponse delete(@PathVariable("id") String id
    ) {
        configValueService.delete(id);
        return RestResponse.ok("delete successfully");
    }

    @PostMapping(value = "/clear")
    public RestResponse clear(@RequestParam(value = "key", required = false) String key
    ) {
        configValueService.deleteCache(key);
        return RestResponse.ok("delete successfully");
    }


}

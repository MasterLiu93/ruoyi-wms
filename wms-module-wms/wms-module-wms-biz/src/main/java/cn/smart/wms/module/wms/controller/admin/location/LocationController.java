package cn.smart.wms.module.wms.controller.admin.location;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

import cn.smart.wms.framework.excel.core.util.ExcelUtils;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.smart.wms.module.wms.controller.admin.location.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.location.LocationDO;
import cn.smart.wms.module.wms.service.location.LocationService;

@Tag(name = "管理后台 - 库位")
@RestController
@RequestMapping("/wms/location")
@Validated
public class LocationController {

    @Resource
    private LocationService locationService;

    @PostMapping("/create")
    @Operation(summary = "创建库位")
    @PreAuthorize("@ss.hasPermission('wms:location:create')")
    public CommonResult<Long> createLocation(@Valid @RequestBody LocationSaveReqVO createReqVO) {
        return success(locationService.createLocation(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库位")
    @PreAuthorize("@ss.hasPermission('wms:location:update')")
    public CommonResult<Boolean> updateLocation(@Valid @RequestBody LocationSaveReqVO updateReqVO) {
        locationService.updateLocation(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库位")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:location:delete')")
    public CommonResult<Boolean> deleteLocation(@RequestParam("id") Long id) {
        locationService.deleteLocation(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库位")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:location:query')")
    public CommonResult<LocationRespVO> getLocation(@RequestParam("id") Long id) {
        LocationDO location = locationService.getLocation(id);
        return success(BeanUtils.toBean(location, LocationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库位分页")
    @PreAuthorize("@ss.hasPermission('wms:location:query')")
    public CommonResult<PageResult<LocationRespVO>> getLocationPage(@Valid LocationPageReqVO pageReqVO) {
        PageResult<LocationDO> pageResult = locationService.getLocationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, LocationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库位 Excel")
    @PreAuthorize("@ss.hasPermission('wms:location:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportLocationExcel(@Valid LocationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<LocationDO> list = locationService.getLocationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库位.xls", "数据", LocationRespVO.class,
                        BeanUtils.toBean(list, LocationRespVO.class));
    }

}
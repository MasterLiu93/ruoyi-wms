package cn.smart.wms.module.wms.controller.admin.location;

import cn.smart.wms.framework.idgenerator.core.IdGeneratorFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
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
import cn.smart.wms.module.wms.service.rack.RackService;
import cn.smart.wms.module.wms.service.area.WmsAreaService;
import cn.smart.wms.module.wms.service.warehouse.WarehouseService;
import cn.smart.wms.module.wms.dal.dataobject.rack.RackDO;
import cn.smart.wms.module.wms.dal.dataobject.area.AreaDO;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;

@Tag(name = "管理后台 - 库位")
@RestController
@RequestMapping("/wms/location")
@Validated
public class LocationController {

    @Resource
    private LocationService locationService;
    
    @Resource
    private RackService rackService;
    
    @Resource
    private WmsAreaService areaService;
    
    @Resource
    private WarehouseService warehouseService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @PostMapping("/create")
    @Operation(summary = "创建库位")
    @PreAuthorize("@ss.hasPermission('wms:location:create')")
    public CommonResult<Long> createLocation(@Valid @RequestBody LocationSaveReqVO createReqVO) {
        // 自动生成库位编码，不再依赖前端传入
        if (createReqVO.getLocationCode() == null || createReqVO.getLocationCode().isEmpty()) {
            createReqVO.setLocationCode(idGeneratorFactory.generateLocationCode());
        }
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
        LocationRespVO respVO = BeanUtils.toBean(location, LocationRespVO.class);
        
        // 填充货架、货区和仓库信息
        if (location != null && location.getRackId() != null) {
            RackDO rack = rackService.getRack(location.getRackId());
            if (rack != null) {
                respVO.setRackName(rack.getRackName());
                respVO.setAreaId(rack.getAreaId());
                
                // 填充货区和仓库信息
                if (rack.getAreaId() != null) {
                    AreaDO area = areaService.getArea(rack.getAreaId());
                    if (area != null) {
                        respVO.setAreaName(area.getAreaName());
                        respVO.setWarehouseId(area.getWarehouseId());
                        
                        // 填充仓库信息
                        if (area.getWarehouseId() != null) {
                            WarehouseDO warehouse = warehouseService.getWarehouse(area.getWarehouseId());
                            if (warehouse != null) {
                                respVO.setWarehouseName(warehouse.getWarehouseName());
                            }
                        }
                    }
                }
            }
        }
        
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得库位分页")
    @PreAuthorize("@ss.hasPermission('wms:location:query')")
    public CommonResult<PageResult<LocationRespVO>> getLocationPage(@Valid LocationPageReqVO pageReqVO) {
        PageResult<LocationDO> pageResult = locationService.getLocationPage(pageReqVO);
        PageResult<LocationRespVO> respPageResult = BeanUtils.toBean(pageResult, LocationRespVO.class);
        
        // 填充货架、货区和仓库信息
        if (CollUtil.isNotEmpty(respPageResult.getList())) {
            // 获取货架ID列表
            Set<Long> rackIds = respPageResult.getList().stream()
                                       .map(LocationRespVO::getRackId)
                                       .filter(Objects::nonNull)
                                       .collect(Collectors.toSet());
            
            if (CollUtil.isNotEmpty(rackIds)) {
                // 批量查询货架信息
                List<RackDO> racks = rackService.getRackList(rackIds);
                Map<Long, RackDO> rackMap = racks.stream()
                                               .collect(Collectors.toMap(RackDO::getId, rack -> rack));
                
                // 获取货区ID列表
                Set<Long> areaIds = racks.stream()
                                     .map(RackDO::getAreaId)
                                     .filter(Objects::nonNull)
                                     .collect(Collectors.toSet());
                
                if (CollUtil.isNotEmpty(areaIds)) {
                    // 批量查询货区信息
                    List<AreaDO> areas = areaService.getAreaList(areaIds);
                    Map<Long, AreaDO> areaMap = areas.stream()
                                                 .collect(Collectors.toMap(AreaDO::getId, area -> area));
                    
                    // 获取仓库ID列表
                    Set<Long> warehouseIds = areas.stream()
                                             .map(AreaDO::getWarehouseId)
                                             .filter(Objects::nonNull)
                                             .collect(Collectors.toSet());
                    
                    // 批量查询仓库信息
                    Map<Long, WarehouseDO> warehouseMap = warehouseService.getWarehouseMap(warehouseIds);
                    
                    // 填充关联信息
                    respPageResult.getList().forEach(locationResp -> {
                        if (locationResp.getRackId() != null && rackMap.containsKey(locationResp.getRackId())) {
                            RackDO rack = rackMap.get(locationResp.getRackId());
                            locationResp.setRackName(rack.getRackName());
                            locationResp.setAreaId(rack.getAreaId());
                            
                            // 填充货区信息
                            if (rack.getAreaId() != null && areaMap.containsKey(rack.getAreaId())) {
                                AreaDO area = areaMap.get(rack.getAreaId());
                                locationResp.setAreaName(area.getAreaName());
                                locationResp.setWarehouseId(area.getWarehouseId());
                                
                                // 填充仓库信息
                                if (area.getWarehouseId() != null && warehouseMap.containsKey(area.getWarehouseId())) {
                                    locationResp.setWarehouseName(warehouseMap.get(area.getWarehouseId()).getWarehouseName());
                                }
                            }
                        }
                    });
                }
            }
        }
        
        return success(respPageResult);
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

    @GetMapping("/simple-list")
    @Operation(summary = "获得简单库位列表")
    @PreAuthorize("@ss.hasPermission('wms:location:query')")
    public CommonResult<List<LocationRespVO>> getSimpleLocationList(@RequestParam(value = "rackId", required = false) Long rackId) {
        List<LocationDO> list = locationService.getLocationList(rackId);
        List<LocationRespVO> respList = BeanUtils.toBean(list, LocationRespVO.class);
        
        // 填充货架、货区和仓库信息
        if (CollUtil.isNotEmpty(respList)) {
            // 获取货架ID列表
            Set<Long> rackIds = respList.stream()
                                   .map(LocationRespVO::getRackId)
                                   .filter(Objects::nonNull)
                                   .collect(Collectors.toSet());
            
            if (CollUtil.isNotEmpty(rackIds)) {
                // 批量查询货架信息
                List<RackDO> racks = rackService.getRackList(rackIds);
                Map<Long, RackDO> rackMap = racks.stream()
                                           .collect(Collectors.toMap(RackDO::getId, rack -> rack));
                
                // 获取货区ID列表
                Set<Long> areaIds = racks.stream()
                                 .map(RackDO::getAreaId)
                                 .filter(Objects::nonNull)
                                 .collect(Collectors.toSet());
                
                if (CollUtil.isNotEmpty(areaIds)) {
                    // 批量查询货区信息
                    List<AreaDO> areas = areaService.getAreaList(areaIds);
                    Map<Long, AreaDO> areaMap = areas.stream()
                                             .collect(Collectors.toMap(AreaDO::getId, area -> area));
                    
                    // 获取仓库ID列表
                    Set<Long> warehouseIds = areas.stream()
                                         .map(AreaDO::getWarehouseId)
                                         .filter(Objects::nonNull)
                                         .collect(Collectors.toSet());
                    
                    // 批量查询仓库信息
                    Map<Long, WarehouseDO> warehouseMap = warehouseService.getWarehouseMap(warehouseIds);
                    
                    // 填充关联信息
                    respList.forEach(locationResp -> {
                        if (locationResp.getRackId() != null && rackMap.containsKey(locationResp.getRackId())) {
                            RackDO rack = rackMap.get(locationResp.getRackId());
                            locationResp.setRackName(rack.getRackName());
                            locationResp.setAreaId(rack.getAreaId());
                            
                            // 填充货区信息
                            if (rack.getAreaId() != null && areaMap.containsKey(rack.getAreaId())) {
                                AreaDO area = areaMap.get(rack.getAreaId());
                                locationResp.setAreaName(area.getAreaName());
                                locationResp.setWarehouseId(area.getWarehouseId());
                                
                                // 填充仓库信息
                                if (area.getWarehouseId() != null && warehouseMap.containsKey(area.getWarehouseId())) {
                                    locationResp.setWarehouseName(warehouseMap.get(area.getWarehouseId()).getWarehouseName());
                                }
                            }
                        }
                    });
                }
            }
        }
        
        return success(respList);
    }
}
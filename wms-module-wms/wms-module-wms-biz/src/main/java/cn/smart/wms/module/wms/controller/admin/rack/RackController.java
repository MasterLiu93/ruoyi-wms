package cn.smart.wms.module.wms.controller.admin.rack;

import cn.hutool.core.collection.CollUtil;
import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.excel.core.util.ExcelUtils;
import cn.smart.wms.framework.idgenerator.core.IdGeneratorFactory;
import cn.smart.wms.module.wms.controller.admin.rack.vo.RackPageReqVO;
import cn.smart.wms.module.wms.controller.admin.rack.vo.RackRespVO;
import cn.smart.wms.module.wms.controller.admin.rack.vo.RackSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.area.AreaDO;
import cn.smart.wms.module.wms.dal.dataobject.rack.RackDO;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.smart.wms.module.wms.service.area.WmsAreaService;
import cn.smart.wms.module.wms.service.rack.RackService;
import cn.smart.wms.module.wms.service.warehouse.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 货架")
@RestController
@RequestMapping("/wms/rack")
@Validated
public class RackController {

    @Resource
    private RackService rackService;
    
    @Resource
    private WmsAreaService areaService;
    
    @Resource
    private WarehouseService warehouseService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @PostMapping("/create")
    @Operation(summary = "创建货架")
    @PreAuthorize("@ss.hasPermission('wms:rack:create')")
    public CommonResult<Long> createRack(@Valid @RequestBody RackSaveReqVO createReqVO) {
        // 自动生成货架编码，不再依赖前端传入
        if (createReqVO.getRackCode() == null || createReqVO.getRackCode().isEmpty()) {
            createReqVO.setRackCode(idGeneratorFactory.generateCustomCode("HJ", 1));
        }
        return success(rackService.createRack(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新货架")
    @PreAuthorize("@ss.hasPermission('wms:rack:update')")
    public CommonResult<Boolean> updateRack(@Valid @RequestBody RackSaveReqVO updateReqVO) {
        rackService.updateRack(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除货架")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:rack:delete')")
    public CommonResult<Boolean> deleteRack(@RequestParam("id") Long id) {
        rackService.deleteRack(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得货架")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:rack:query')")
    public CommonResult<RackRespVO> getRack(@RequestParam("id") Long id) {
        RackDO rack = rackService.getRack(id);
        RackRespVO respVO = BeanUtils.toBean(rack, RackRespVO.class);
        
        // 填充货区和仓库信息
        if (rack != null && rack.getAreaId() != null) {
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
        
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得货架分页")
    @PreAuthorize("@ss.hasPermission('wms:rack:query')")
    public CommonResult<PageResult<RackRespVO>> getRackPage(@Valid RackPageReqVO pageReqVO) {
        PageResult<RackDO> pageResult = rackService.getRackPage(pageReqVO);
        PageResult<RackRespVO> respPageResult = BeanUtils.toBean(pageResult, RackRespVO.class);
        
        // 填充货区和仓库信息
        if (CollUtil.isNotEmpty(respPageResult.getList())) {
            // 获取货区ID列表
            Set<Long> areaIds = respPageResult.getList().stream()
                                        .map(RackRespVO::getAreaId)
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
                
                // 填充货区和仓库信息
                respPageResult.getList().forEach(rackResp -> {
                    if (rackResp.getAreaId() != null && areaMap.containsKey(rackResp.getAreaId())) {
                        AreaDO area = areaMap.get(rackResp.getAreaId());
                        rackResp.setAreaName(area.getAreaName());
                        rackResp.setWarehouseId(area.getWarehouseId());
                        
                        // 填充仓库信息
                        if (area.getWarehouseId() != null && warehouseMap.containsKey(area.getWarehouseId())) {
                            rackResp.setWarehouseName(warehouseMap.get(area.getWarehouseId()).getWarehouseName());
                        }
                    }
                });
            }
        }
        
        return success(respPageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出货架 Excel")
    @PreAuthorize("@ss.hasPermission('wms:rack:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRackExcel(@Valid RackPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<RackDO> list = rackService.getRackPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "货架.xls", "数据", RackRespVO.class,
                        BeanUtils.toBean(list, RackRespVO.class));
    }
    
    @GetMapping("/simple-list")
    @Operation(summary = "获得简单货架列表")
    @PreAuthorize("@ss.hasPermission('wms:rack:query')")
    public CommonResult<List<RackRespVO>> getSimpleRackList(@RequestParam(value = "areaId", required = false) Long areaId) {
        List<RackDO> list = rackService.getRackListByAreaId(areaId);
        List<RackRespVO> respList = BeanUtils.toBean(list, RackRespVO.class);
        
        // 填充货区和仓库信息
        if (CollUtil.isNotEmpty(respList)) {
            // 获取货区ID列表
            Set<Long> areaIds = respList.stream()
                                    .map(RackRespVO::getAreaId)
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
                
                // 填充货区和仓库信息
                respList.forEach(rackResp -> {
                    if (rackResp.getAreaId() != null && areaMap.containsKey(rackResp.getAreaId())) {
                        AreaDO area = areaMap.get(rackResp.getAreaId());
                        rackResp.setAreaName(area.getAreaName());
                        rackResp.setWarehouseId(area.getWarehouseId());
                        
                        // 填充仓库信息
                        if (area.getWarehouseId() != null && warehouseMap.containsKey(area.getWarehouseId())) {
                            rackResp.setWarehouseName(warehouseMap.get(area.getWarehouseId()).getWarehouseName());
                        }
                    }
                });
            }
        }
        
        return success(respList);
    }
}
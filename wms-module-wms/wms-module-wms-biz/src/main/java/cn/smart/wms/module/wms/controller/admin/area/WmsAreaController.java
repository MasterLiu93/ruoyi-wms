package cn.smart.wms.module.wms.controller.admin.area;

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

import cn.smart.wms.module.wms.controller.admin.area.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.area.AreaDO;
import cn.smart.wms.module.wms.service.area.WmsAreaService;
import cn.smart.wms.module.wms.service.warehouse.WarehouseService;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;

@Tag(name = "管理后台 - 货区")
@RestController
@RequestMapping("/wms/area")
@Validated
public class WmsAreaController {

    @Resource
    private WmsAreaService areaService;
    
    @Resource
    private WarehouseService warehouseService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @PostMapping("/create")
    @Operation(summary = "创建货区")
    @PreAuthorize("@ss.hasPermission('wms:area:create')")
    public CommonResult<Long> createArea(@Valid @RequestBody AreaSaveReqVO createReqVO) {
        // 自动生成货区编码，不再依赖前端传入
        if (createReqVO.getAreaCode() == null || createReqVO.getAreaCode().isEmpty()) {
            createReqVO.setAreaCode(idGeneratorFactory.generateCustomCode("HQ", 1));
        }
        return success(areaService.createArea(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新货区")
    @PreAuthorize("@ss.hasPermission('wms:area:update')")
    public CommonResult<Boolean> updateArea(@Valid @RequestBody AreaSaveReqVO updateReqVO) {
        areaService.updateArea(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除货区")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:area:delete')")
    public CommonResult<Boolean> deleteArea(@RequestParam("id") Long id) {
        areaService.deleteArea(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得货区")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:area:query')")
    public CommonResult<AreaRespVO> getArea(@RequestParam("id") Long id) {
        AreaDO area = areaService.getArea(id);
        AreaRespVO respVO = BeanUtils.toBean(area, AreaRespVO.class);
        // 填充仓库信息
        if (area != null && area.getWarehouseId() != null) {
            WarehouseDO warehouse = warehouseService.getWarehouse(area.getWarehouseId());
            if (warehouse != null) {
                respVO.setWarehouseName(warehouse.getWarehouseName());
            }
        }
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得货区分页")
    @PreAuthorize("@ss.hasPermission('wms:area:query')")
    public CommonResult<PageResult<AreaRespVO>> getAreaPage(@Valid AreaPageReqVO pageReqVO) {
        PageResult<AreaDO> pageResult = areaService.getAreaPage(pageReqVO);
        PageResult<AreaRespVO> respPageResult = BeanUtils.toBean(pageResult, AreaRespVO.class);
        
        // 填充仓库信息
        if (CollUtil.isNotEmpty(respPageResult.getList())) {
            // 获取仓库ID列表
            Set<Long> warehouseIds = respPageResult.getList().stream()
                                              .map(AreaRespVO::getWarehouseId)
                                              .filter(Objects::nonNull)
                                              .collect(Collectors.toSet());
            // 批量查询仓库信息
            Map<Long, WarehouseDO> warehouseMap = warehouseService.getWarehouseMap(warehouseIds);
            // 填充仓库名称
            respPageResult.getList().forEach(areaResp -> {
                if (areaResp.getWarehouseId() != null && warehouseMap.containsKey(areaResp.getWarehouseId())) {
                    areaResp.setWarehouseName(warehouseMap.get(areaResp.getWarehouseId()).getWarehouseName());
                }
            });
        }
        
        return success(respPageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出货区 Excel")
    @PreAuthorize("@ss.hasPermission('wms:area:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAreaExcel(@Valid AreaPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AreaDO> list = areaService.getAreaPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "货区.xls", "数据", AreaRespVO.class,
                        BeanUtils.toBean(list, AreaRespVO.class));
    }
    
    @GetMapping("/simple-list")
    @Operation(summary = "获得简单货区列表")
    @PreAuthorize("@ss.hasPermission('wms:area:query')")
    public CommonResult<List<AreaRespVO>> getSimpleAreaList(@RequestParam(value = "warehouseId", required = false) Long warehouseId) {
        List<AreaDO> list = areaService.getAreaListByWarehouseId(warehouseId);
        // 转换为VO，并填充仓库信息
        List<AreaRespVO> respList = BeanUtils.toBean(list, AreaRespVO.class);
        if (CollUtil.isNotEmpty(respList)) {
            // 获取所有仓库ID
            Set<Long> warehouseIds = respList.stream()
                                        .map(AreaRespVO::getWarehouseId)
                                        .filter(Objects::nonNull)
                                        .collect(Collectors.toSet());
            // 批量查询仓库信息
            if (CollUtil.isNotEmpty(warehouseIds)) {
                Map<Long, WarehouseDO> warehouseMap = warehouseService.getWarehouseMap(warehouseIds);
                // 填充仓库名称
                respList.forEach(areaResp -> {
                    if (areaResp.getWarehouseId() != null && warehouseMap.containsKey(areaResp.getWarehouseId())) {
                        areaResp.setWarehouseName(warehouseMap.get(areaResp.getWarehouseId()).getWarehouseName());
                    }
                });
            }
        }
        return success(respList);
    }

}
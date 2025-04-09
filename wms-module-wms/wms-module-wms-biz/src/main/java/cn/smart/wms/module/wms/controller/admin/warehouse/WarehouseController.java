package cn.smart.wms.module.wms.controller.admin.warehouse;

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

import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

import cn.smart.wms.framework.excel.core.util.ExcelUtils;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.smart.wms.module.wms.controller.admin.warehouse.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;
import cn.smart.wms.module.wms.service.warehouse.WarehouseService;

@Tag(name = "管理后台 - 仓库")
@RestController
@RequestMapping("/wms/warehouse")
@Validated
public class WarehouseController {

    @Resource
    private WarehouseService warehouseService;
    
    @Resource
    private IdGeneratorFactory idGeneratorFactory;

    @PostMapping("/create")
    @Operation(summary = "创建仓库")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:create')")
    public CommonResult<Long> createWarehouse(@Valid @RequestBody WarehouseSaveReqVO createReqVO) {
        // 自动生成仓库编码，不再依赖前端传入
        if (createReqVO.getWarehouseCode() == null || createReqVO.getWarehouseCode().isEmpty()) {
            createReqVO.setWarehouseCode(idGeneratorFactory.generateCustomCode("CK", 1));
        }
        return success(warehouseService.createWarehouse(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新仓库")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:update')")
    public CommonResult<Boolean> updateWarehouse(@Valid @RequestBody WarehouseSaveReqVO updateReqVO) {
        warehouseService.updateWarehouse(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除仓库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:warehouse:delete')")
    public CommonResult<Boolean> deleteWarehouse(@RequestParam("id") Long id) {
        warehouseService.deleteWarehouse(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得仓库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<WarehouseRespVO> getWarehouse(@RequestParam("id") Long id) {
        WarehouseDO warehouse = warehouseService.getWarehouse(id);
        return success(BeanUtils.toBean(warehouse, WarehouseRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得仓库分页")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<PageResult<WarehouseRespVO>> getWarehousePage(@Valid WarehousePageReqVO pageVO) {
        PageResult<WarehouseDO> pageResult = warehouseService.getWarehousePage(pageVO);
        return success(BeanUtils.toBean(pageResult, WarehouseRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出仓库 Excel")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWarehouseExcel(@Valid WarehousePageReqVO pageVO,
              HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WarehouseDO> list = warehouseService.getWarehousePage(pageVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "仓库数据.xls", "数据", WarehouseRespVO.class,
                BeanUtils.toBean(list, WarehouseRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得简单仓库列表")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<List<WarehouseSimpleRespVO>> getSimpleWarehouseList() {
        List<WarehouseDO> list = warehouseService.getWarehouseList();
        return success(BeanUtils.toBean(list, WarehouseSimpleRespVO.class));
    }

}
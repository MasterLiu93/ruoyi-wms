package cn.smart.wms.module.wms.controller.admin.inventorymovement;

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

import cn.smart.wms.module.wms.controller.admin.inventorymovement.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorymovement.InventoryMovementDO;
import cn.smart.wms.module.wms.service.inventorymovement.InventoryMovementService;

@Tag(name = "管理后台 - 库存移动记录")
@RestController
@RequestMapping("/wms/inventory-movement")
@Validated
public class InventoryMovementController {

    @Resource
    private InventoryMovementService inventoryMovementService;

    @PostMapping("/create")
    @Operation(summary = "创建库存移动记录")
    @PreAuthorize("@ss.hasPermission('wms:inventory-movement:create')")
    public CommonResult<Long> createInventoryMovement(@Valid @RequestBody InventoryMovementSaveReqVO createReqVO) {
        return success(inventoryMovementService.createInventoryMovement(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存移动记录")
    @PreAuthorize("@ss.hasPermission('wms:inventory-movement:update')")
    public CommonResult<Boolean> updateInventoryMovement(@Valid @RequestBody InventoryMovementSaveReqVO updateReqVO) {
        inventoryMovementService.updateInventoryMovement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存移动记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:inventory-movement:delete')")
    public CommonResult<Boolean> deleteInventoryMovement(@RequestParam("id") Long id) {
        inventoryMovementService.deleteInventoryMovement(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存移动记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:inventory-movement:query')")
    public CommonResult<InventoryMovementRespVO> getInventoryMovement(@RequestParam("id") Long id) {
        InventoryMovementDO inventoryMovement = inventoryMovementService.getInventoryMovement(id);
        return success(BeanUtils.toBean(inventoryMovement, InventoryMovementRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存移动记录分页")
    @PreAuthorize("@ss.hasPermission('wms:inventory-movement:query')")
    public CommonResult<PageResult<InventoryMovementRespVO>> getInventoryMovementPage(@Valid InventoryMovementPageReqVO pageReqVO) {
        PageResult<InventoryMovementDO> pageResult = inventoryMovementService.getInventoryMovementPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InventoryMovementRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存移动记录 Excel")
    @PreAuthorize("@ss.hasPermission('wms:inventory-movement:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInventoryMovementExcel(@Valid InventoryMovementPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InventoryMovementDO> list = inventoryMovementService.getInventoryMovementPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存移动记录.xls", "数据", InventoryMovementRespVO.class,
                        BeanUtils.toBean(list, InventoryMovementRespVO.class));
    }

}
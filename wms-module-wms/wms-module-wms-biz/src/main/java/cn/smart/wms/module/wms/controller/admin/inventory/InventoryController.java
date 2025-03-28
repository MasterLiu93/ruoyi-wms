package cn.smart.wms.module.wms.controller.admin.inventory;

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

import cn.smart.wms.module.wms.controller.admin.inventory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import cn.smart.wms.module.wms.service.inventory.InventoryService;

@Tag(name = "管理后台 - 库存")
@RestController
@RequestMapping("/wms/inventory")
@Validated
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @PostMapping("/create")
    @Operation(summary = "创建库存")
    @PreAuthorize("@ss.hasPermission('wms:inventory:create')")
    public CommonResult<Long> createInventory(@Valid @RequestBody InventorySaveReqVO createReqVO) {
        return success(inventoryService.createInventory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存")
    @PreAuthorize("@ss.hasPermission('wms:inventory:update')")
    public CommonResult<Boolean> updateInventory(@Valid @RequestBody InventorySaveReqVO updateReqVO) {
        inventoryService.updateInventory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:inventory:delete')")
    public CommonResult<Boolean> deleteInventory(@RequestParam("id") Long id) {
        inventoryService.deleteInventory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:inventory:query')")
    public CommonResult<InventoryRespVO> getInventory(@RequestParam("id") Long id) {
        InventoryDO inventory = inventoryService.getInventory(id);
        return success(BeanUtils.toBean(inventory, InventoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存分页")
    @PreAuthorize("@ss.hasPermission('wms:inventory:query')")
    public CommonResult<PageResult<InventoryRespVO>> getInventoryPage(@Valid InventoryPageReqVO pageReqVO) {
        PageResult<InventoryDO> pageResult = inventoryService.getInventoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InventoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存 Excel")
    @PreAuthorize("@ss.hasPermission('wms:inventory:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInventoryExcel(@Valid InventoryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InventoryDO> list = inventoryService.getInventoryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存.xls", "数据", InventoryRespVO.class,
                        BeanUtils.toBean(list, InventoryRespVO.class));
    }

}
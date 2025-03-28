package cn.smart.wms.module.wms.controller.admin.inventorycheck;

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

import cn.smart.wms.module.wms.controller.admin.inventorycheck.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheck.InventoryCheckDO;
import cn.smart.wms.module.wms.service.inventorycheck.InventoryCheckService;

@Tag(name = "管理后台 - 库存盘点单")
@RestController
@RequestMapping("/wms/inventory-check")
@Validated
public class InventoryCheckController {

    @Resource
    private InventoryCheckService inventoryCheckService;

    @PostMapping("/create")
    @Operation(summary = "创建库存盘点单")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:create')")
    public CommonResult<Long> createInventoryCheck(@Valid @RequestBody InventoryCheckSaveReqVO createReqVO) {
        return success(inventoryCheckService.createInventoryCheck(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存盘点单")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:update')")
    public CommonResult<Boolean> updateInventoryCheck(@Valid @RequestBody InventoryCheckSaveReqVO updateReqVO) {
        inventoryCheckService.updateInventoryCheck(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存盘点单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:delete')")
    public CommonResult<Boolean> deleteInventoryCheck(@RequestParam("id") Long id) {
        inventoryCheckService.deleteInventoryCheck(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存盘点单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:query')")
    public CommonResult<InventoryCheckRespVO> getInventoryCheck(@RequestParam("id") Long id) {
        InventoryCheckDO inventoryCheck = inventoryCheckService.getInventoryCheck(id);
        return success(BeanUtils.toBean(inventoryCheck, InventoryCheckRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存盘点单分页")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:query')")
    public CommonResult<PageResult<InventoryCheckRespVO>> getInventoryCheckPage(@Valid InventoryCheckPageReqVO pageReqVO) {
        PageResult<InventoryCheckDO> pageResult = inventoryCheckService.getInventoryCheckPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InventoryCheckRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存盘点单 Excel")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInventoryCheckExcel(@Valid InventoryCheckPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InventoryCheckDO> list = inventoryCheckService.getInventoryCheckPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存盘点单.xls", "数据", InventoryCheckRespVO.class,
                        BeanUtils.toBean(list, InventoryCheckRespVO.class));
    }

}
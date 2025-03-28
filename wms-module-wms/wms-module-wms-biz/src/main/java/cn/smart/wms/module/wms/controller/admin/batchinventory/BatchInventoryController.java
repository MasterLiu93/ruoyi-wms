package cn.smart.wms.module.wms.controller.admin.batchinventory;

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

import cn.smart.wms.module.wms.controller.admin.batchinventory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.batchinventory.BatchInventoryDO;
import cn.smart.wms.module.wms.service.batchinventory.BatchInventoryService;

@Tag(name = "管理后台 - 批次库存")
@RestController
@RequestMapping("/wms/batch-inventory")
@Validated
public class BatchInventoryController {

    @Resource
    private BatchInventoryService batchInventoryService;

    @PostMapping("/create")
    @Operation(summary = "创建批次库存")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:create')")
    public CommonResult<Long> createBatchInventory(@Valid @RequestBody BatchInventorySaveReqVO createReqVO) {
        return success(batchInventoryService.createBatchInventory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新批次库存")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:update')")
    public CommonResult<Boolean> updateBatchInventory(@Valid @RequestBody BatchInventorySaveReqVO updateReqVO) {
        batchInventoryService.updateBatchInventory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除批次库存")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:delete')")
    public CommonResult<Boolean> deleteBatchInventory(@RequestParam("id") Long id) {
        batchInventoryService.deleteBatchInventory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得批次库存")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:query')")
    public CommonResult<BatchInventoryRespVO> getBatchInventory(@RequestParam("id") Long id) {
        BatchInventoryDO batchInventory = batchInventoryService.getBatchInventory(id);
        return success(BeanUtils.toBean(batchInventory, BatchInventoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得批次库存分页")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:query')")
    public CommonResult<PageResult<BatchInventoryRespVO>> getBatchInventoryPage(@Valid BatchInventoryPageReqVO pageReqVO) {
        PageResult<BatchInventoryDO> pageResult = batchInventoryService.getBatchInventoryPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BatchInventoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出批次库存 Excel")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchInventoryExcel(@Valid BatchInventoryPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchInventoryDO> list = batchInventoryService.getBatchInventoryPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "批次库存.xls", "数据", BatchInventoryRespVO.class,
                        BeanUtils.toBean(list, BatchInventoryRespVO.class));
    }

}
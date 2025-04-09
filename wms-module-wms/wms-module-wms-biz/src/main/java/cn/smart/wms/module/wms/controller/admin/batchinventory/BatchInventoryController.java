package cn.smart.wms.module.wms.controller.admin.batchinventory;

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
    public CommonResult<PageResult<BatchInventoryRespVO>> getBatchInventoryPage(@Valid BatchInventoryPageReqVO pageVO) {
        PageResult<BatchInventoryDO> pageResult = batchInventoryService.getBatchInventoryPage(pageVO);
        return success(BeanUtils.toBean(pageResult, BatchInventoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出批次库存 Excel")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchInventoryExcel(@Valid BatchInventoryPageReqVO pageVO,
              HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchInventoryDO> list = batchInventoryService.getBatchInventoryPage(pageVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "批次库存数据.xls", "数据", BatchInventoryRespVO.class,
                        BeanUtils.toBean(list, BatchInventoryRespVO.class));
    }
    
    @GetMapping("/get-by-info")
    @Operation(summary = "根据批次、物料、仓库和库位获取批次库存")
    @Parameter(name = "batchId", description = "批次ID", required = true, example = "1001")
    @Parameter(name = "itemId", description = "物料ID", required = true, example = "2001")
    @Parameter(name = "warehouseId", description = "仓库ID", required = true, example = "3001")
    @Parameter(name = "locationId", description = "库位ID", required = true, example = "4001")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:query')")
    public CommonResult<BatchInventoryRespVO> getBatchInventoryByInfo(@RequestParam("batchId") Long batchId,
                                                          @RequestParam("itemId") Long itemId,
                                                          @RequestParam("warehouseId") Long warehouseId,
                                                          @RequestParam("locationId") Long locationId) {
        BatchInventoryDO batchInventory = batchInventoryService.getBatchInventoryByInfo(batchId, itemId, warehouseId, locationId);
        return success(BeanUtils.toBean(batchInventory, BatchInventoryRespVO.class));
    }
    
    @GetMapping("/check-sufficient")
    @Operation(summary = "检查批次库存是否足够")
    @Parameter(name = "batchId", description = "批次ID", required = true, example = "1001")
    @Parameter(name = "itemId", description = "物料ID", required = true, example = "2001")
    @Parameter(name = "warehouseId", description = "仓库ID", required = true, example = "3001")
    @Parameter(name = "locationId", description = "库位ID", required = true, example = "4001")
    @Parameter(name = "count", description = "数量", required = true, example = "100")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:query')")
    public CommonResult<Boolean> checkBatchInventorySufficient(@RequestParam("batchId") Long batchId,
                                                   @RequestParam("itemId") Long itemId,
                                                   @RequestParam("warehouseId") Long warehouseId,
                                                   @RequestParam("locationId") Long locationId,
                                                   @RequestParam("count") Integer count) {
        boolean sufficient = batchInventoryService.isBatchInventorySufficient(batchId, itemId, warehouseId, locationId, count);
        return success(sufficient);
    }
    
    @GetMapping("/get-total-available")
    @Operation(summary = "获取批次在所有库位的可用库存总量")
    @Parameter(name = "batchId", description = "批次ID", required = true, example = "1001")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:query')")
    public CommonResult<Integer> getTotalAvailableCount(@RequestParam("batchId") Long batchId) {
        Integer availableCount = batchInventoryService.getTotalAvailableCountByBatchId(batchId);
        return success(availableCount);
    }
    
    @GetMapping("/get-warehouse-available")
    @Operation(summary = "获取批次在指定仓库的可用库存总量")
    @Parameter(name = "batchId", description = "批次ID", required = true, example = "1001")
    @Parameter(name = "warehouseId", description = "仓库ID", required = true, example = "3001")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:query')")
    public CommonResult<Integer> getWarehouseAvailableCount(@RequestParam("batchId") Long batchId,
                                                     @RequestParam("warehouseId") Long warehouseId) {
        Integer availableCount = batchInventoryService.getAvailableCountByBatchAndWarehouse(batchId, warehouseId);
        return success(availableCount);
    }
    
    @PostMapping("/increase")
    @Operation(summary = "增加批次库存")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:update')")
    public CommonResult<BatchInventoryRespVO> increaseBatchInventory(@Valid @RequestBody BatchInventoryOperationReqVO reqVO) {
        BatchInventoryDO batchInventory = batchInventoryService.increaseBatchInventory(
            reqVO.getBatchId(), reqVO.getItemId(), reqVO.getWarehouseId(), reqVO.getLocationId(), reqVO.getCount(),
            reqVO.getBusinessType(), reqVO.getBusinessId(), reqVO.getBusinessDetailId(), reqVO.getRemark()
        );
        return success(BeanUtils.toBean(batchInventory, BatchInventoryRespVO.class));
    }
    
    @PostMapping("/decrease")
    @Operation(summary = "减少批次库存")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:update')")
    public CommonResult<BatchInventoryRespVO> decreaseBatchInventory(@Valid @RequestBody BatchInventoryOperationReqVO reqVO) {
        BatchInventoryDO batchInventory = batchInventoryService.decreaseBatchInventory(
            reqVO.getBatchId(), reqVO.getItemId(), reqVO.getWarehouseId(), reqVO.getLocationId(), reqVO.getCount(),
            reqVO.getBusinessType(), reqVO.getBusinessId(), reqVO.getBusinessDetailId(), reqVO.getRemark()
        );
        return success(BeanUtils.toBean(batchInventory, BatchInventoryRespVO.class));
    }
    
    @PostMapping("/lock")
    @Operation(summary = "锁定批次库存")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:update')")
    public CommonResult<Boolean> lockBatchInventory(@Valid @RequestBody BatchInventoryOperationReqVO reqVO) {
        batchInventoryService.lockBatchInventory(
            reqVO.getBatchId(), reqVO.getItemId(), reqVO.getWarehouseId(), reqVO.getLocationId(), reqVO.getCount(),
            reqVO.getBusinessType(), reqVO.getBusinessId(), reqVO.getBusinessDetailId(), reqVO.getRemark()
        );
        return success(true);
    }
    
    @PostMapping("/unlock")
    @Operation(summary = "解锁批次库存")
    @PreAuthorize("@ss.hasPermission('wms:batch-inventory:update')")
    public CommonResult<Boolean> unlockBatchInventory(@Valid @RequestBody BatchInventoryOperationReqVO reqVO) {
        batchInventoryService.unlockBatchInventory(
            reqVO.getBatchId(), reqVO.getItemId(), reqVO.getWarehouseId(), reqVO.getLocationId(), reqVO.getCount(),
            reqVO.getBusinessType(), reqVO.getBusinessId(), reqVO.getBusinessDetailId(), reqVO.getRemark()
        );
        return success(true);
    }
}
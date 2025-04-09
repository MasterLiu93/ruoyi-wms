package cn.smart.wms.module.wms.controller.admin.inventorycheck;

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

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

import cn.smart.wms.framework.excel.core.util.ExcelUtils;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.smart.wms.module.wms.controller.admin.inventorycheck.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheck.InventoryCheckDO;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheckdetail.InventoryCheckDetailDO;
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
    public CommonResult<InventoryCheckDO> getInventoryCheck(@RequestParam("id") Long id) {
        return success(inventoryCheckService.getInventoryCheck(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存盘点单分页")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:query')")
    public CommonResult<PageResult<InventoryCheckDO>> getInventoryCheckPage(@Valid InventoryCheckPageReqVO pageVO) {
        return success(inventoryCheckService.getInventoryCheckPage(pageVO));
    }

    @PostMapping("/generate-plan")
    @Operation(summary = "生成盘点计划")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:create')")
    public CommonResult<Long> generateCheckPlan(@Valid @RequestBody InventoryCheckPlanReqVO reqVO) {
        return success(inventoryCheckService.generateCheckPlan(
            reqVO.getWarehouseId(), reqVO.getCheckType(), 
            reqVO.getLocationIds(), reqVO.getItemIds(), reqVO.getRemark()
        ));
    }

    @PostMapping("/start")
    @Operation(summary = "开始盘点")
    @Parameter(name = "id", description = "盘点单ID", required = true)
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:update')")
    public CommonResult<Boolean> startCheck(@RequestParam("id") Long id) {
        inventoryCheckService.startCheck(id);
        return success(true);
    }

    @PostMapping("/submit-result")
    @Operation(summary = "提交盘点结果")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:update')")
    public CommonResult<InventoryCheckDetailDO> submitCheckResult(@Valid @RequestBody InventoryCheckResultReqVO reqVO) {
        return success(inventoryCheckService.submitCheckResult(reqVO.getDetailId(), reqVO.getCheckCount(), reqVO.getRemark()));
    }

    @PostMapping("/batch-submit-result")
    @Operation(summary = "批量提交盘点结果")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:update')")
    public CommonResult<List<Long>> batchSubmitCheckResult(@Valid @RequestBody InventoryCheckBatchResultReqVO reqVO) {
        return success(inventoryCheckService.batchSubmitCheckResult(reqVO.getCheckId(), reqVO.getCheckDetails()));
    }

    @PostMapping("/complete")
    @Operation(summary = "完成盘点")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:update')")
    public CommonResult<Boolean> completeCheck(@Valid @RequestBody InventoryCheckCompleteReqVO reqVO) {
        inventoryCheckService.completeCheck(reqVO.getId(), reqVO.getAutoAdjust());
        return success(true);
    }

    @PostMapping("/adjust")
    @Operation(summary = "调整库存差异")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:update')")
    public CommonResult<Boolean> adjustInventory(@Valid @RequestBody InventoryCheckAdjustReqVO reqVO) {
        inventoryCheckService.adjustInventory(reqVO.getId(), reqVO.getRemark());
        return success(true);
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消盘点")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:update')")
    public CommonResult<Boolean> cancelCheck(@Valid @RequestBody InventoryCheckCancelReqVO reqVO) {
        inventoryCheckService.cancelCheck(reqVO.getId(), reqVO.getRemark());
        return success(true);
    }

    @GetMapping("/progress")
    @Operation(summary = "获取盘点进度")
    @Parameter(name = "id", description = "盘点单ID", required = true)
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:query')")
    public CommonResult<Map<String, Object>> getCheckProgress(@RequestParam("id") Long id) {
        return success(inventoryCheckService.getCheckProgress(id));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存盘点单 Excel")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInventoryCheckExcel(@Valid InventoryCheckPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        // 设置为导出全部数据
        pageReqVO.setPageNo(1);
        pageReqVO.setPageSize(Integer.MAX_VALUE);
        
        // 查询数据
        List<InventoryCheckDO> list = inventoryCheckService.getInventoryCheckPage(pageReqVO).getList();
        
        // 转换为 VO 对象
        List<InventoryCheckRespVO> voList = new ArrayList<>();
        for (InventoryCheckDO check : list) {
            voList.add(BeanUtils.toBean(check, InventoryCheckRespVO.class));
        }
        
        // 导出 Excel
        ExcelUtils.write(response, "库存盘点单.xls", "数据", InventoryCheckRespVO.class, voList);
    }

}
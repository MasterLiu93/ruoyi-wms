package cn.smart.wms.module.wms.controller.admin.receiptorder;

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

import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptOrderSaveReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptOrderRespVO;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptOrderPageReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptOrderApproveReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.ReceiptDetailOperationReqVO;
import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.ReceiptOrderDetailRespVO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorder.ReceiptOrderDO;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;
import cn.smart.wms.module.wms.service.receiptorder.ReceiptOrderService;
import cn.smart.wms.module.wms.service.receiptorderdetail.ReceiptOrderDetailService;

/**
 * 后台管理 - 入库单
 */
@Tag(name = "后台管理 - 入库单")
@RestController
@RequestMapping("/wms/receipt-order")
@Validated
public class ReceiptOrderController {

    @Resource
    private ReceiptOrderService receiptOrderService;
    
    @Resource
    private ReceiptOrderDetailService receiptOrderDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建入库单")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:create')")
    public CommonResult<Long> createReceiptOrder(@Valid @RequestBody ReceiptOrderSaveReqVO createReqVO) {
        return success(receiptOrderService.createReceiptOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新入库单")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:update')")
    public CommonResult<Boolean> updateReceiptOrder(@Valid @RequestBody ReceiptOrderSaveReqVO updateReqVO) {
        receiptOrderService.updateReceiptOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除入库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:delete')")
    public CommonResult<Boolean> deleteReceiptOrder(@RequestParam("id") Long id) {
        receiptOrderService.deleteReceiptOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得入库单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:query')")
    public CommonResult<ReceiptOrderRespVO> getReceiptOrder(@RequestParam("id") Long id) {
        ReceiptOrderDO receiptOrder = receiptOrderService.getReceiptOrder(id);
        // 转换VO
        ReceiptOrderRespVO receiptOrderRespVO = BeanUtils.toBean(receiptOrder, ReceiptOrderRespVO.class);
        if (receiptOrderRespVO != null) {
            // 获取明细
            List<ReceiptOrderDetailDO> details = receiptOrderDetailService.getReceiptOrderDetailListByOrderId(id);
            List<ReceiptOrderDetailRespVO> detailVOList = BeanUtils.toBean(details, ReceiptOrderDetailRespVO.class);
            receiptOrderRespVO.setDetails(detailVOList);
        }
        return success(receiptOrderRespVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得入库单分页")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:query')")
    public CommonResult<PageResult<ReceiptOrderRespVO>> getReceiptOrderPage(@Valid ReceiptOrderPageReqVO pageReqVO) {
        PageResult<ReceiptOrderDO> pageResult = receiptOrderService.getReceiptOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ReceiptOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出入库单 Excel")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportReceiptOrderExcel(@Valid ReceiptOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ReceiptOrderDO> list = receiptOrderService.getReceiptOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "入库单.xls", "数据", ReceiptOrderRespVO.class,
                        BeanUtils.toBean(list, ReceiptOrderRespVO.class));
    }
    
    @PostMapping("/submit")
    @Operation(summary = "提交入库单审核")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:submit')")
    public CommonResult<Boolean> submitReceiptOrder(@RequestParam("id") Long id) {
        receiptOrderService.submitReceiptOrder(id);
        return success(true);
    }
    
    @PostMapping("/approve")
    @Operation(summary = "审核入库单")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:approve')")
    public CommonResult<Boolean> approveReceiptOrder(@Valid @RequestBody ReceiptOrderApproveReqVO approveReqVO) {
        receiptOrderService.approveReceiptOrder(approveReqVO.getId(), approveReqVO.getApproved(), approveReqVO.getRemark());
        return success(true);
    }
    
    @PostMapping("/receipt")
    @Operation(summary = "执行入库操作")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:receipt')")
    public CommonResult<Map<String, Object>> executeReceipt(@Valid @RequestBody ReceiptDetailOperationReqVO receiptReqVO) {
        // 执行入库操作
        Long recordId = receiptOrderService.executeReceiptByDetail(
            receiptReqVO.getDetailId(), 
            receiptReqVO.getLocationId(), 
            receiptReqVO.getBatchId(), 
            receiptReqVO.getCount(), 
            receiptReqVO.getRemark()
        );
        
        // 获取明细对象
        ReceiptOrderDetailDO detail = receiptOrderDetailService.getReceiptOrderDetail(receiptReqVO.getDetailId());
        if (detail == null) {
            return success(Collections.singletonMap("recordId", recordId));
        }
        
        // 获取入库单最新状态
        ReceiptOrderDO receiptOrder = receiptOrderService.getReceiptOrder(detail.getReceiptOrderId());
        
        // 构建返回信息
        Map<String, Object> result = new HashMap<>();
        result.put("recordId", recordId);
        result.put("orderStatus", receiptOrder.getReceiptStatus());
        result.put("detailStatus", detail.getStatus());
        result.put("needRefresh", true); // 告诉前端需要刷新页面
        
        return success(result);
    }
    
    @PostMapping("/complete")
    @Operation(summary = "完成入库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:receipt')")
    public CommonResult<Boolean> completeReceiptOrder(@RequestParam("id") Long id) {
        receiptOrderService.executeReceiptOrder(id);
        return success(true);
    }
    
    @PostMapping("/cancel")
    @Operation(summary = "取消入库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:update')")
    public CommonResult<Boolean> cancelReceiptOrder(@RequestParam("id") Long id) {
        receiptOrderService.cancelReceiptOrder(id);
        return success(true);
    }

    @PostMapping("/create-with-details")
    @Operation(summary = "创建入库单和明细（一次性保存）")
    @ApiAccessLog(operateType = CREATE)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:create')")
    public CommonResult<Long> createReceiptOrderWithDetails(@Valid @RequestBody ReceiptOrderSaveReqVO createReqVO) {
        return success(receiptOrderService.createReceiptOrderWithDetails(createReqVO));
    }

    @PutMapping("/update-with-details")
    @Operation(summary = "更新入库单和明细（一次性保存）")
    @ApiAccessLog(operateType = UPDATE)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:update')")
    public CommonResult<Boolean> updateReceiptOrderWithDetails(@Valid @RequestBody ReceiptOrderSaveReqVO updateReqVO) {
        receiptOrderService.updateReceiptOrderWithDetails(updateReqVO);
        return success(true);
    }

    @PostMapping("/submit-and-receipt")
    @Operation(summary = "提交入库单并直接入库（一步到位）")
    @ApiAccessLog(operateType = UPDATE)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:update')")
    public CommonResult<Long> submitAndReceiptOrder(@Valid @RequestBody ReceiptOrderSaveReqVO reqVO) {
        return success(receiptOrderService.submitAndReceiptOrder(reqVO));
    }
    
    @PostMapping("/batch-receipt")
    @Operation(summary = "批量执行入库操作")
    @Parameter(name = "id", description = "入库单编号", required = true)
    @ApiAccessLog(operateType = UPDATE)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order:receipt')")
    public CommonResult<Map<String, Object>> batchExecuteReceipt(@RequestParam("id") Long id) {
        // 执行批量入库操作
        receiptOrderService.batchExecuteReceiptByOrderId(id);
        
        // 获取入库单最新状态
        ReceiptOrderDO receiptOrder = receiptOrderService.getReceiptOrder(id);
        
        // 构建返回信息
        Map<String, Object> result = new HashMap<>();
        result.put("orderStatus", receiptOrder.getOrderStatus());
        result.put("receiptStatus", receiptOrder.getReceiptStatus());
        result.put("needRefresh", true); // 告诉前端需要刷新页面
        
        return success(result);
    }
}
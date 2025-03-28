package cn.smart.wms.module.wms.controller.admin.receiptorder;

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

import cn.smart.wms.module.wms.controller.admin.receiptorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptorder.ReceiptOrderDO;
import cn.smart.wms.module.wms.service.receiptorder.ReceiptOrderService;

@Tag(name = "管理后台 - 入库单")
@RestController
@RequestMapping("/wms/receipt-order")
@Validated
public class ReceiptOrderController {

    @Resource
    private ReceiptOrderService receiptOrderService;

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
        return success(BeanUtils.toBean(receiptOrder, ReceiptOrderRespVO.class));
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

}
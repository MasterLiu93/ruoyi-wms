package cn.smart.wms.module.wms.controller.admin.receiptorderdetail;

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

import cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptorderdetail.ReceiptOrderDetailDO;
import cn.smart.wms.module.wms.service.receiptorderdetail.ReceiptOrderDetailService;

@Tag(name = "管理后台 - 入库单明细")
@RestController
@RequestMapping("/wms/receipt-order-detail")
@Validated
public class ReceiptOrderDetailController {

    @Resource
    private ReceiptOrderDetailService receiptOrderDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建入库单明细")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:create')")
    public CommonResult<Long> createReceiptOrderDetail(@Valid @RequestBody ReceiptOrderDetailSaveReqVO createReqVO) {
        return success(receiptOrderDetailService.createReceiptOrderDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新入库单明细")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:update')")
    public CommonResult<Boolean> updateReceiptOrderDetail(@Valid @RequestBody ReceiptOrderDetailSaveReqVO updateReqVO) {
        receiptOrderDetailService.updateReceiptOrderDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除入库单明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:delete')")
    public CommonResult<Boolean> deleteReceiptOrderDetail(@RequestParam("id") Long id) {
        receiptOrderDetailService.deleteReceiptOrderDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得入库单明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:query')")
    public CommonResult<ReceiptOrderDetailRespVO> getReceiptOrderDetail(@RequestParam("id") Long id) {
        ReceiptOrderDetailDO receiptOrderDetail = receiptOrderDetailService.getReceiptOrderDetail(id);
        return success(BeanUtils.toBean(receiptOrderDetail, ReceiptOrderDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得入库单明细分页")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:query')")
    public CommonResult<PageResult<ReceiptOrderDetailRespVO>> getReceiptOrderDetailPage(@Valid ReceiptOrderDetailPageReqVO pageReqVO) {
        PageResult<ReceiptOrderDetailDO> pageResult = receiptOrderDetailService.getReceiptOrderDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ReceiptOrderDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出入库单明细 Excel")
    @PreAuthorize("@ss.hasPermission('wms:receipt-order-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportReceiptOrderDetailExcel(@Valid ReceiptOrderDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ReceiptOrderDetailDO> list = receiptOrderDetailService.getReceiptOrderDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "入库单明细.xls", "数据", ReceiptOrderDetailRespVO.class,
                        BeanUtils.toBean(list, ReceiptOrderDetailRespVO.class));
    }

}
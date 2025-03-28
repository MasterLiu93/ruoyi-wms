package cn.smart.wms.module.wms.controller.admin.receiptrecord;

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

import cn.smart.wms.module.wms.controller.admin.receiptrecord.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.receiptrecord.ReceiptRecordDO;
import cn.smart.wms.module.wms.service.receiptrecord.ReceiptRecordService;

@Tag(name = "管理后台 - 入库操作记录")
@RestController
@RequestMapping("/wms/receipt-record")
@Validated
public class ReceiptRecordController {

    @Resource
    private ReceiptRecordService receiptRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建入库操作记录")
    @PreAuthorize("@ss.hasPermission('wms:receipt-record:create')")
    public CommonResult<Long> createReceiptRecord(@Valid @RequestBody ReceiptRecordSaveReqVO createReqVO) {
        return success(receiptRecordService.createReceiptRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新入库操作记录")
    @PreAuthorize("@ss.hasPermission('wms:receipt-record:update')")
    public CommonResult<Boolean> updateReceiptRecord(@Valid @RequestBody ReceiptRecordSaveReqVO updateReqVO) {
        receiptRecordService.updateReceiptRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除入库操作记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:receipt-record:delete')")
    public CommonResult<Boolean> deleteReceiptRecord(@RequestParam("id") Long id) {
        receiptRecordService.deleteReceiptRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得入库操作记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:receipt-record:query')")
    public CommonResult<ReceiptRecordRespVO> getReceiptRecord(@RequestParam("id") Long id) {
        ReceiptRecordDO receiptRecord = receiptRecordService.getReceiptRecord(id);
        return success(BeanUtils.toBean(receiptRecord, ReceiptRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得入库操作记录分页")
    @PreAuthorize("@ss.hasPermission('wms:receipt-record:query')")
    public CommonResult<PageResult<ReceiptRecordRespVO>> getReceiptRecordPage(@Valid ReceiptRecordPageReqVO pageReqVO) {
        PageResult<ReceiptRecordDO> pageResult = receiptRecordService.getReceiptRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ReceiptRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出入库操作记录 Excel")
    @PreAuthorize("@ss.hasPermission('wms:receipt-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportReceiptRecordExcel(@Valid ReceiptRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ReceiptRecordDO> list = receiptRecordService.getReceiptRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "入库操作记录.xls", "数据", ReceiptRecordRespVO.class,
                        BeanUtils.toBean(list, ReceiptRecordRespVO.class));
    }

}
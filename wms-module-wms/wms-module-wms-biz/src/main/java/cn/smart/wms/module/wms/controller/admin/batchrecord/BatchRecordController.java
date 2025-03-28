package cn.smart.wms.module.wms.controller.admin.batchrecord;

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

import cn.smart.wms.module.wms.controller.admin.batchrecord.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.batchrecord.BatchRecordDO;
import cn.smart.wms.module.wms.service.batchrecord.BatchRecordService;

@Tag(name = "管理后台 - 批次操作记录")
@RestController
@RequestMapping("/wms/batch-record")
@Validated
public class BatchRecordController {

    @Resource
    private BatchRecordService batchRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建批次操作记录")
    @PreAuthorize("@ss.hasPermission('wms:batch-record:create')")
    public CommonResult<Long> createBatchRecord(@Valid @RequestBody BatchRecordSaveReqVO createReqVO) {
        return success(batchRecordService.createBatchRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新批次操作记录")
    @PreAuthorize("@ss.hasPermission('wms:batch-record:update')")
    public CommonResult<Boolean> updateBatchRecord(@Valid @RequestBody BatchRecordSaveReqVO updateReqVO) {
        batchRecordService.updateBatchRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除批次操作记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:batch-record:delete')")
    public CommonResult<Boolean> deleteBatchRecord(@RequestParam("id") Long id) {
        batchRecordService.deleteBatchRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得批次操作记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:batch-record:query')")
    public CommonResult<BatchRecordRespVO> getBatchRecord(@RequestParam("id") Long id) {
        BatchRecordDO batchRecord = batchRecordService.getBatchRecord(id);
        return success(BeanUtils.toBean(batchRecord, BatchRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得批次操作记录分页")
    @PreAuthorize("@ss.hasPermission('wms:batch-record:query')")
    public CommonResult<PageResult<BatchRecordRespVO>> getBatchRecordPage(@Valid BatchRecordPageReqVO pageReqVO) {
        PageResult<BatchRecordDO> pageResult = batchRecordService.getBatchRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BatchRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出批次操作记录 Excel")
    @PreAuthorize("@ss.hasPermission('wms:batch-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchRecordExcel(@Valid BatchRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchRecordDO> list = batchRecordService.getBatchRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "批次操作记录.xls", "数据", BatchRecordRespVO.class,
                        BeanUtils.toBean(list, BatchRecordRespVO.class));
    }

}
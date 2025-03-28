package cn.smart.wms.module.wms.controller.admin.batch;

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

import cn.smart.wms.module.wms.controller.admin.batch.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.batch.BatchDO;
import cn.smart.wms.module.wms.service.batch.BatchService;

@Tag(name = "管理后台 - 批次信息")
@RestController
@RequestMapping("/wms/batch")
@Validated
public class BatchController {

    @Resource
    private BatchService batchService;

    @PostMapping("/create")
    @Operation(summary = "创建批次信息")
    @PreAuthorize("@ss.hasPermission('wms:batch:create')")
    public CommonResult<Long> createBatch(@Valid @RequestBody BatchSaveReqVO createReqVO) {
        return success(batchService.createBatch(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新批次信息")
    @PreAuthorize("@ss.hasPermission('wms:batch:update')")
    public CommonResult<Boolean> updateBatch(@Valid @RequestBody BatchSaveReqVO updateReqVO) {
        batchService.updateBatch(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除批次信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:batch:delete')")
    public CommonResult<Boolean> deleteBatch(@RequestParam("id") Long id) {
        batchService.deleteBatch(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得批次信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:batch:query')")
    public CommonResult<BatchRespVO> getBatch(@RequestParam("id") Long id) {
        BatchDO batch = batchService.getBatch(id);
        return success(BeanUtils.toBean(batch, BatchRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得批次信息分页")
    @PreAuthorize("@ss.hasPermission('wms:batch:query')")
    public CommonResult<PageResult<BatchRespVO>> getBatchPage(@Valid BatchPageReqVO pageReqVO) {
        PageResult<BatchDO> pageResult = batchService.getBatchPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BatchRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出批次信息 Excel")
    @PreAuthorize("@ss.hasPermission('wms:batch:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchExcel(@Valid BatchPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchDO> list = batchService.getBatchPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "批次信息.xls", "数据", BatchRespVO.class,
                        BeanUtils.toBean(list, BatchRespVO.class));
    }

}
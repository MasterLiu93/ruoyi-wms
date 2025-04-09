package cn.smart.wms.module.wms.controller.admin.batch;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.excel.core.util.ExcelUtils;
import cn.smart.wms.module.wms.controller.admin.batch.vo.BatchPageReqVO;
import cn.smart.wms.module.wms.controller.admin.batch.vo.BatchRespVO;
import cn.smart.wms.module.wms.controller.admin.batch.vo.BatchSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.batch.BatchDO;
import cn.smart.wms.module.wms.service.batch.BatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

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
    public CommonResult<PageResult<BatchRespVO>> getBatchPage(@Valid BatchPageReqVO pageVO) {
        PageResult<BatchDO> pageResult = batchService.getBatchPage(pageVO);
        return success(BeanUtils.toBean(pageResult, BatchRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出批次信息 Excel")
    @PreAuthorize("@ss.hasPermission('wms:batch:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchExcel(@Valid BatchPageReqVO pageVO,
              HttpServletResponse response) throws IOException {
        pageVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchDO> list = batchService.getBatchPage(pageVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "批次信息数据.xls", "数据", BatchRespVO.class,
                        BeanUtils.toBean(list, BatchRespVO.class));
    }
    
    @GetMapping("/get-by-batch-no")
    @Operation(summary = "根据批次号获取批次信息")
    @Parameter(name = "batchNo", description = "批次号", required = true, example = "B20230101001")
    @PreAuthorize("@ss.hasPermission('wms:batch:query')")
    public CommonResult<BatchRespVO> getBatchByBatchNo(@RequestParam("batchNo") String batchNo) {
        BatchDO batch = batchService.getBatchByBatchNo(batchNo);
        return success(BeanUtils.toBean(batch, BatchRespVO.class));
    }
    
    @GetMapping("/check-expired")
    @Operation(summary = "检查批次是否过期")
    @Parameter(name = "id", description = "批次ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:batch:query')")
    public CommonResult<Boolean> checkBatchExpired(@RequestParam("id") Long id) {
        boolean expired = batchService.isBatchExpired(id);
        return success(expired);
    }
    
    @PutMapping("/freeze")
    @Operation(summary = "冻结批次")
    @Parameter(name = "id", description = "批次ID", required = true, example = "1024")
    @Parameter(name = "remark", description = "备注", example = "质量问题冻结")
    @PreAuthorize("@ss.hasPermission('wms:batch:update')")
    public CommonResult<Boolean> freezeBatch(@RequestParam("id") Long id, @RequestParam(value = "remark", required = false) String remark) {
        batchService.freezeBatch(id, remark);
        return success(true);
    }
    
    @PutMapping("/unfreeze")
    @Operation(summary = "解冻批次")
    @Parameter(name = "id", description = "批次ID", required = true, example = "1024")
    @Parameter(name = "remark", description = "备注", example = "质量问题已解决")
    @PreAuthorize("@ss.hasPermission('wms:batch:update')")
    public CommonResult<Boolean> unfreezeBatch(@RequestParam("id") Long id, @RequestParam(value = "remark", required = false) String remark) {
        batchService.unfreezeBatch(id, remark);
        return success(true);
    }
}
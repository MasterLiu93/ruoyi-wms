package cn.smart.wms.module.wms.controller.admin.moverecord;

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

import cn.smart.wms.module.wms.controller.admin.moverecord.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moverecord.MoveRecordDO;
import cn.smart.wms.module.wms.service.moverecord.MoveRecordService;

@Tag(name = "管理后台 - 移库操作记录")
@RestController
@RequestMapping("/wms/move-record")
@Validated
public class MoveRecordController {

    @Resource
    private MoveRecordService moveRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建移库操作记录")
    @PreAuthorize("@ss.hasPermission('wms:move-record:create')")
    public CommonResult<Long> createMoveRecord(@Valid @RequestBody MoveRecordSaveReqVO createReqVO) {
        return success(moveRecordService.createMoveRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新移库操作记录")
    @PreAuthorize("@ss.hasPermission('wms:move-record:update')")
    public CommonResult<Boolean> updateMoveRecord(@Valid @RequestBody MoveRecordSaveReqVO updateReqVO) {
        moveRecordService.updateMoveRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除移库操作记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:move-record:delete')")
    public CommonResult<Boolean> deleteMoveRecord(@RequestParam("id") Long id) {
        moveRecordService.deleteMoveRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得移库操作记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:move-record:query')")
    public CommonResult<MoveRecordRespVO> getMoveRecord(@RequestParam("id") Long id) {
        MoveRecordDO moveRecord = moveRecordService.getMoveRecord(id);
        return success(BeanUtils.toBean(moveRecord, MoveRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得移库操作记录分页")
    @PreAuthorize("@ss.hasPermission('wms:move-record:query')")
    public CommonResult<PageResult<MoveRecordRespVO>> getMoveRecordPage(@Valid MoveRecordPageReqVO pageReqVO) {
        PageResult<MoveRecordDO> pageResult = moveRecordService.getMoveRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MoveRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出移库操作记录 Excel")
    @PreAuthorize("@ss.hasPermission('wms:move-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMoveRecordExcel(@Valid MoveRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MoveRecordDO> list = moveRecordService.getMoveRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "移库操作记录.xls", "数据", MoveRecordRespVO.class,
                        BeanUtils.toBean(list, MoveRecordRespVO.class));
    }

}
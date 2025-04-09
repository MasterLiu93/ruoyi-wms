package cn.smart.wms.module.wms.controller.admin.moveorderdetail;

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

import cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moveorderdetail.MoveOrderDetailDO;
import cn.smart.wms.module.wms.service.moveorderdetail.MoveOrderDetailService;

@Tag(name = "管理后台 - 移库单明细")
@RestController
@RequestMapping("/wms/move-order-detail")
@Validated
public class MoveOrderDetailController {

    @Resource
    private MoveOrderDetailService moveOrderDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建移库单明细")
    @PreAuthorize("@ss.hasPermission('wms:move-order-detail:create')")
    public CommonResult<Long> createMoveOrderDetail(@Valid @RequestBody MoveOrderDetailSaveReqVO createReqVO) {
        return success(moveOrderDetailService.createMoveOrderDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新移库单明细")
    @PreAuthorize("@ss.hasPermission('wms:move-order-detail:update')")
    public CommonResult<Boolean> updateMoveOrderDetail(@Valid @RequestBody MoveOrderDetailSaveReqVO updateReqVO) {
        moveOrderDetailService.updateMoveOrderDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除移库单明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:move-order-detail:delete')")
    public CommonResult<Boolean> deleteMoveOrderDetail(@RequestParam("id") Long id) {
        moveOrderDetailService.deleteMoveOrderDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得移库单明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:move-order-detail:query')")
    public CommonResult<MoveOrderDetailRespVO> getMoveOrderDetail(@RequestParam("id") Long id) {
        MoveOrderDetailDO moveOrderDetail = moveOrderDetailService.getMoveOrderDetail(id);
        return success(BeanUtils.toBean(moveOrderDetail, MoveOrderDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得移库单明细分页")
    @PreAuthorize("@ss.hasPermission('wms:move-order-detail:query')")
    public CommonResult<PageResult<MoveOrderDetailRespVO>> getMoveOrderDetailPage(@Valid MoveOrderDetailPageReqVO pageReqVO) {
        PageResult<MoveOrderDetailDO> pageResult = moveOrderDetailService.getMoveOrderDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MoveOrderDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出移库单明细 Excel")
    @PreAuthorize("@ss.hasPermission('wms:move-order-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMoveOrderDetailExcel(@Valid MoveOrderDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MoveOrderDetailDO> list = moveOrderDetailService.getMoveOrderDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "移库单明细.xls", "数据", MoveOrderDetailRespVO.class,
                        BeanUtils.toBean(list, MoveOrderDetailRespVO.class));
    }

}
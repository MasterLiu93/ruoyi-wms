package cn.smart.wms.module.wms.controller.admin.moveorder;

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

import cn.smart.wms.module.wms.controller.admin.moveorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moveorder.MoveOrderDO;
import cn.smart.wms.module.wms.service.moveorder.MoveOrderService;

@Tag(name = "管理后台 - 移库单")
@RestController
@RequestMapping("/wms/move-order")
@Validated
public class MoveOrderController {

    @Resource
    private MoveOrderService moveOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建移库单")
    @PreAuthorize("@ss.hasPermission('wms:move-order:create')")
    public CommonResult<Long> createMoveOrder(@Valid @RequestBody MoveOrderSaveReqVO createReqVO) {
        return success(moveOrderService.createMoveOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新移库单")
    @PreAuthorize("@ss.hasPermission('wms:move-order:update')")
    public CommonResult<Boolean> updateMoveOrder(@Valid @RequestBody MoveOrderSaveReqVO updateReqVO) {
        moveOrderService.updateMoveOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除移库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:move-order:delete')")
    public CommonResult<Boolean> deleteMoveOrder(@RequestParam("id") Long id) {
        moveOrderService.deleteMoveOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得移库单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:move-order:query')")
    public CommonResult<MoveOrderRespVO> getMoveOrder(@RequestParam("id") Long id) {
        MoveOrderDO moveOrder = moveOrderService.getMoveOrder(id);
        return success(BeanUtils.toBean(moveOrder, MoveOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得移库单分页")
    @PreAuthorize("@ss.hasPermission('wms:move-order:query')")
    public CommonResult<PageResult<MoveOrderRespVO>> getMoveOrderPage(@Valid MoveOrderPageReqVO pageReqVO) {
        PageResult<MoveOrderDO> pageResult = moveOrderService.getMoveOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MoveOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出移库单 Excel")
    @PreAuthorize("@ss.hasPermission('wms:move-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMoveOrderExcel(@Valid MoveOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MoveOrderDO> list = moveOrderService.getMoveOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "移库单.xls", "数据", MoveOrderRespVO.class,
                        BeanUtils.toBean(list, MoveOrderRespVO.class));
    }

}
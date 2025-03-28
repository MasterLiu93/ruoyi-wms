package cn.smart.wms.module.wms.controller.admin.inventorycheckdetail;

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

import cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheckdetail.InventoryCheckDetailDO;
import cn.smart.wms.module.wms.service.inventorycheckdetail.InventoryCheckDetailService;

@Tag(name = "管理后台 - 库存盘点明细")
@RestController
@RequestMapping("/wms/inventory-check-detail")
@Validated
public class InventoryCheckDetailController {

    @Resource
    private InventoryCheckDetailService inventoryCheckDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建库存盘点明细")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check-detail:create')")
    public CommonResult<Long> createInventoryCheckDetail(@Valid @RequestBody InventoryCheckDetailSaveReqVO createReqVO) {
        return success(inventoryCheckDetailService.createInventoryCheckDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存盘点明细")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check-detail:update')")
    public CommonResult<Boolean> updateInventoryCheckDetail(@Valid @RequestBody InventoryCheckDetailSaveReqVO updateReqVO) {
        inventoryCheckDetailService.updateInventoryCheckDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存盘点明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:inventory-check-detail:delete')")
    public CommonResult<Boolean> deleteInventoryCheckDetail(@RequestParam("id") Long id) {
        inventoryCheckDetailService.deleteInventoryCheckDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存盘点明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check-detail:query')")
    public CommonResult<InventoryCheckDetailRespVO> getInventoryCheckDetail(@RequestParam("id") Long id) {
        InventoryCheckDetailDO inventoryCheckDetail = inventoryCheckDetailService.getInventoryCheckDetail(id);
        return success(BeanUtils.toBean(inventoryCheckDetail, InventoryCheckDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存盘点明细分页")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check-detail:query')")
    public CommonResult<PageResult<InventoryCheckDetailRespVO>> getInventoryCheckDetailPage(@Valid InventoryCheckDetailPageReqVO pageReqVO) {
        PageResult<InventoryCheckDetailDO> pageResult = inventoryCheckDetailService.getInventoryCheckDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InventoryCheckDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存盘点明细 Excel")
    @PreAuthorize("@ss.hasPermission('wms:inventory-check-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInventoryCheckDetailExcel(@Valid InventoryCheckDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InventoryCheckDetailDO> list = inventoryCheckDetailService.getInventoryCheckDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存盘点明细.xls", "数据", InventoryCheckDetailRespVO.class,
                        BeanUtils.toBean(list, InventoryCheckDetailRespVO.class));
    }

}
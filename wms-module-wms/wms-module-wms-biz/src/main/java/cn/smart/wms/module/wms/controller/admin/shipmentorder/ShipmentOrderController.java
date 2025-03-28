package cn.smart.wms.module.wms.controller.admin.shipmentorder;

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

import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorder.ShipmentOrderDO;
import cn.smart.wms.module.wms.service.shipmentorder.ShipmentOrderService;

@Tag(name = "管理后台 - 出库单")
@RestController
@RequestMapping("/wms/shipment-order")
@Validated
public class ShipmentOrderController {

    @Resource
    private ShipmentOrderService shipmentOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建出库单")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:create')")
    public CommonResult<Long> createShipmentOrder(@Valid @RequestBody ShipmentOrderSaveReqVO createReqVO) {
        return success(shipmentOrderService.createShipmentOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新出库单")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:update')")
    public CommonResult<Boolean> updateShipmentOrder(@Valid @RequestBody ShipmentOrderSaveReqVO updateReqVO) {
        shipmentOrderService.updateShipmentOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除出库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:delete')")
    public CommonResult<Boolean> deleteShipmentOrder(@RequestParam("id") Long id) {
        shipmentOrderService.deleteShipmentOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得出库单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:query')")
    public CommonResult<ShipmentOrderRespVO> getShipmentOrder(@RequestParam("id") Long id) {
        ShipmentOrderDO shipmentOrder = shipmentOrderService.getShipmentOrder(id);
        return success(BeanUtils.toBean(shipmentOrder, ShipmentOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得出库单分页")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:query')")
    public CommonResult<PageResult<ShipmentOrderRespVO>> getShipmentOrderPage(@Valid ShipmentOrderPageReqVO pageReqVO) {
        PageResult<ShipmentOrderDO> pageResult = shipmentOrderService.getShipmentOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShipmentOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出出库单 Excel")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShipmentOrderExcel(@Valid ShipmentOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShipmentOrderDO> list = shipmentOrderService.getShipmentOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "出库单.xls", "数据", ShipmentOrderRespVO.class,
                        BeanUtils.toBean(list, ShipmentOrderRespVO.class));
    }

}
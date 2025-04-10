package cn.smart.wms.module.wms.controller.admin.shipmentorder;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.excel.core.util.ExcelUtils;
import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.ShipmentOrderApproveReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.ShipmentOrderPageReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.ShipmentOrderRespVO;
import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.ShipmentOrderSaveReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.ShipmentOperationReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailRespVO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorder.ShipmentOrderDO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO;
import cn.smart.wms.module.wms.service.shipmentorder.ShipmentOrderService;
import cn.smart.wms.module.wms.service.shipmentorderdetail.ShipmentOrderDetailService;
import cn.smart.wms.module.wms.service.item.ItemService;
import cn.smart.wms.module.wms.service.inventory.InventoryService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.smart.wms.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.smart.wms.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 出库单")
@RestController
@RequestMapping("/wms/shipment-order")
@Validated
public class ShipmentOrderController {

    @Resource
    private ShipmentOrderService shipmentOrderService;
    
    @Resource
    private ShipmentOrderDetailService shipmentOrderDetailService;
    
    @Resource
    private ItemService itemService;
    
    @Resource
    private InventoryService inventoryService;

    @PostMapping("/create")
    @Operation(summary = "创建出库单")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:create')")
    public CommonResult<Long> createShipmentOrder(@Valid @RequestBody ShipmentOrderSaveReqVO createReqVO) {
        return success(shipmentOrderService.createShipmentOrderWithDetails(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新出库单")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:update')")
    public CommonResult<Boolean> updateShipmentOrder(@Valid @RequestBody ShipmentOrderSaveReqVO updateReqVO) {
        shipmentOrderService.updateShipmentOrderWithDetails(updateReqVO);
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
        // 转换VO
        ShipmentOrderRespVO shipmentOrderRespVO = BeanUtils.toBean(shipmentOrder, ShipmentOrderRespVO.class);
        if (shipmentOrderRespVO != null) {
            // 获取明细
            List<ShipmentOrderDetailDO> details = shipmentOrderDetailService.getShipmentOrderDetailListByOrderId(id);
            // 转换明细VO并完善物料信息
            List<ShipmentOrderDetailRespVO> detailVOList = new ArrayList<>();
            for (ShipmentOrderDetailDO detail : details) {
                ShipmentOrderDetailRespVO detailVO = BeanUtils.toBean(detail, ShipmentOrderDetailRespVO.class);
                // 补充物料信息 - 实际项目中应该从物料服务中获取这些信息
                // 这里需要根据实际项目结构调用物料服务
                cn.smart.wms.module.wms.dal.dataobject.item.ItemDO item = itemService.getItem(detail.getItemId());
                if (item != null) {
                    detailVO.setItemCode(item.getItemCode());
                    detailVO.setItemName(item.getItemName());
                    detailVO.setItemType(item.getItemType());
                    detailVO.setSpec(item.getSpec());
                    detailVO.setUnit(item.getUnit());
                    
                    // 获取库存数量
                    Integer stockCount = inventoryService.getAvailableCountByWarehouseAndItem(
                        shipmentOrder.getWarehouseId(), item.getId());
                    detailVO.setStockCount(stockCount != null ? stockCount : 0);
                }
                detailVOList.add(detailVO);
            }
            shipmentOrderRespVO.setDetails(detailVOList);
        }
        return success(shipmentOrderRespVO);
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
    
    @PutMapping("/submit")
    @Operation(summary = "提交出库单审核")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:submit')")
    public CommonResult<Boolean> submitShipmentOrder(@RequestParam("id") Long id) {
        shipmentOrderService.submitShipmentOrder(id);
        return success(true);
    }
    
    @PutMapping("/approve")
    @Operation(summary = "审核出库单")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:approve')")
    public CommonResult<Boolean> approveShipmentOrder(@Valid @RequestParam("id") Long id, 
                                                    @RequestParam("approved") Boolean approved, 
                                                    @RequestParam(value = "remark", required = false) String remark) {
        shipmentOrderService.approveShipmentOrder(id, approved, remark);
        return success(true);
    }
    
    @PutMapping("/cancel")
    @Operation(summary = "取消出库单")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:update')")
    public CommonResult<Boolean> cancelShipmentOrder(@Valid @RequestBody Map<String, Object> requestBody) {
        Long id = ((Number) requestBody.get("id")).longValue();
        shipmentOrderService.cancelShipmentOrder(id);
        return success(true);
    }
    
    @PutMapping("/complete")
    @Operation(summary = "完成出库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:update')")
    public CommonResult<Boolean> completeShipmentOrder(@RequestParam("id") Long id) {
        shipmentOrderService.completeShipmentOrder(id);
        return success(true);
    }
    
    @PostMapping("/execute-shipment")
    @Operation(summary = "执行出库操作")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order:update')")
    public CommonResult<Boolean> executeShipment(@Valid @RequestBody ShipmentOperationReqVO reqVO) {
        shipmentOrderService.executeShipment(reqVO);
        return success(true);
    }
}
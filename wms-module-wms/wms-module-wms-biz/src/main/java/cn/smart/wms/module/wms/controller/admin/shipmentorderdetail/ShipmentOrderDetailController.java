package cn.smart.wms.module.wms.controller.admin.shipmentorderdetail;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.excel.core.util.ExcelUtils;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailPageReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailRespVO;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO;
import cn.smart.wms.module.wms.service.shipmentorderdetail.ShipmentOrderDetailService;
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

@Tag(name = "管理后台 - 出库单明细")
@RestController
@RequestMapping("/wms/shipment-order-detail")
@Validated
public class ShipmentOrderDetailController {

    @Resource
    private ShipmentOrderDetailService shipmentOrderDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建出库单明细")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order-detail:create')")
    public CommonResult<Long> createShipmentOrderDetail(@Valid @RequestBody ShipmentOrderDetailSaveReqVO createReqVO) {
        return success(shipmentOrderDetailService.createShipmentOrderDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新出库单明细")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order-detail:update')")
    public CommonResult<Boolean> updateShipmentOrderDetail(@Valid @RequestBody ShipmentOrderDetailSaveReqVO updateReqVO) {
        shipmentOrderDetailService.updateShipmentOrderDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除出库单明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:shipment-order-detail:delete')")
    public CommonResult<Boolean> deleteShipmentOrderDetail(@RequestParam("id") Long id) {
        shipmentOrderDetailService.deleteShipmentOrderDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得出库单明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order-detail:query')")
    public CommonResult<ShipmentOrderDetailRespVO> getShipmentOrderDetail(@RequestParam("id") Long id) {
        ShipmentOrderDetailDO shipmentOrderDetail = shipmentOrderDetailService.getShipmentOrderDetail(id);
        return success(BeanUtils.toBean(shipmentOrderDetail, ShipmentOrderDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得出库单明细分页")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order-detail:query')")
    public CommonResult<PageResult<ShipmentOrderDetailRespVO>> getShipmentOrderDetailPage(@Valid ShipmentOrderDetailPageReqVO pageReqVO) {
        PageResult<ShipmentOrderDetailDO> pageResult = shipmentOrderDetailService.getShipmentOrderDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShipmentOrderDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出出库单明细 Excel")
    @PreAuthorize("@ss.hasPermission('wms:shipment-order-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShipmentOrderDetailExcel(@Valid ShipmentOrderDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShipmentOrderDetailDO> list = shipmentOrderDetailService.getShipmentOrderDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "出库单明细.xls", "数据", ShipmentOrderDetailRespVO.class,
                        BeanUtils.toBean(list, ShipmentOrderDetailRespVO.class));
    }

}
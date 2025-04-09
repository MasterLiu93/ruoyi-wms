package cn.smart.wms.module.wms.controller.admin.shipmentrecord;

import cn.smart.wms.framework.apilog.core.annotation.ApiAccessLog;
import cn.smart.wms.framework.common.pojo.CommonResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.excel.core.util.ExcelUtils;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordPageReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordRespVO;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentrecord.ShipmentRecordDO;
import cn.smart.wms.module.wms.service.shipmentrecord.ShipmentRecordService;
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

@Tag(name = "管理后台 - 出库操作记录")
@RestController
@RequestMapping("/wms/shipment-record")
@Validated
public class ShipmentRecordController {

    @Resource
    private ShipmentRecordService shipmentRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建出库操作记录")
    @PreAuthorize("@ss.hasPermission('wms:shipment-record:create')")
    public CommonResult<Long> createShipmentRecord(@Valid @RequestBody ShipmentRecordSaveReqVO createReqVO) {
        return success(shipmentRecordService.createShipmentRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新出库操作记录")
    @PreAuthorize("@ss.hasPermission('wms:shipment-record:update')")
    public CommonResult<Boolean> updateShipmentRecord(@Valid @RequestBody ShipmentRecordSaveReqVO updateReqVO) {
        shipmentRecordService.updateShipmentRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除出库操作记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:shipment-record:delete')")
    public CommonResult<Boolean> deleteShipmentRecord(@RequestParam("id") Long id) {
        shipmentRecordService.deleteShipmentRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得出库操作记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:shipment-record:query')")
    public CommonResult<ShipmentRecordRespVO> getShipmentRecord(@RequestParam("id") Long id) {
        ShipmentRecordDO shipmentRecord = shipmentRecordService.getShipmentRecord(id);
        return success(BeanUtils.toBean(shipmentRecord, ShipmentRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得出库操作记录分页")
    @PreAuthorize("@ss.hasPermission('wms:shipment-record:query')")
    public CommonResult<PageResult<ShipmentRecordRespVO>> getShipmentRecordPage(@Valid ShipmentRecordPageReqVO pageReqVO) {
        PageResult<ShipmentRecordDO> pageResult = shipmentRecordService.getShipmentRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShipmentRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出出库操作记录 Excel")
    @PreAuthorize("@ss.hasPermission('wms:shipment-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShipmentRecordExcel(@Valid ShipmentRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShipmentRecordDO> list = shipmentRecordService.getShipmentRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "出库操作记录.xls", "数据", ShipmentRecordRespVO.class,
                        BeanUtils.toBean(list, ShipmentRecordRespVO.class));
    }

}
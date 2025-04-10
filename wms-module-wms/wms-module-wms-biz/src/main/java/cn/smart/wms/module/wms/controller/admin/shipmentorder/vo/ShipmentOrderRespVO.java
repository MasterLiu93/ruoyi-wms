package cn.smart.wms.module.wms.controller.admin.shipmentorder.vo;

import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 出库单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShipmentOrderRespVO {

    @Schema(description = "出库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31505")
    @ExcelProperty("出库单ID")
    private Long id;

    @Schema(description = "出库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出库单号")
    private String shipmentOrderNo;

    @Schema(description = "出库类型(0-销售出库 1-生产出库 2-调拨出库 3-其他出库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("出库类型(0-销售出库 1-生产出库 2-调拨出库 3-其他出库)")
    private Integer shipmentType;

    @Schema(description = "客户ID", example = "15163")
    @ExcelProperty("客户ID")
    private Long customerId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3588")
    @ExcelProperty("仓库ID")
    private Long warehouseId;

    @Schema(description = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)")
    private Integer orderStatus;

    @Schema(description = "出库状态(0-待出库 1-部分出库 2-已完成)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("出库状态(0-待出库 1-部分出库 2-已完成)")
    private Integer shipmentStatus;

    @Schema(description = "预计出库时间")
    @ExcelProperty("预计出库时间")
    private LocalDateTime expectTime;

    @Schema(description = "实际完成时间")
    @ExcelProperty("实际完成时间")
    private LocalDateTime completeTime;

    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "7621")
    @ExcelProperty("商品数量")
    private Integer totalCount;

    @Schema(description = "商品金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("商品金额")
    private BigDecimal totalAmount;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "出库单明细列表")
    private List<ShipmentOrderDetailRespVO> details;

}
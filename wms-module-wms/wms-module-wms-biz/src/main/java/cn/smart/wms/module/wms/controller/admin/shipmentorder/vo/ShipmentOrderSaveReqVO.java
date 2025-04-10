package cn.smart.wms.module.wms.controller.admin.shipmentorder.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 出库单新增/修改 Request VO")
@Data
public class ShipmentOrderSaveReqVO {

    @Schema(description = "出库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31505")
    private Long id;

    @Schema(description = "出库单号（可为空，系统会自动生成）")
    private String shipmentOrderNo;

    @Schema(description = "出库类型(0-销售出库 1-生产出库 2-调拨出库 3-其他出库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "出库类型(0-销售出库 1-生产出库 2-调拨出库 3-其他出库)不能为空")
    private Integer shipmentType;

    @Schema(description = "客户ID", example = "15163")
    private Long customerId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3588")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)不能为空")
    private Integer orderStatus;

    @Schema(description = "出库状态(0-待出库 1-部分出库 2-已完成)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "出库状态(0-待出库 1-部分出库 2-已完成)不能为空")
    private Integer shipmentStatus;

    @Schema(description = "预计出库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = "GMT+8")
    private LocalDateTime expectTime;

    @Schema(description = "实际完成时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = "GMT+8")
    private LocalDateTime completeTime;

    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "7621")
    @NotNull(message = "商品数量不能为空")
    private Integer totalCount;

    @Schema(description = "商品金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "商品金额不能为空")
    private BigDecimal totalAmount;

    @Schema(description = "备注", example = "随便")
    private String remark;
    
    @Schema(description = "出库单明细列表")
    private List<cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO> details;
}
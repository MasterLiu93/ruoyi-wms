package cn.smart.wms.module.wms.controller.admin.shipmentorder.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 出库单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShipmentOrderPageReqVO extends PageParam {

    @Schema(description = "出库单号")
    private String shipmentOrderNo;

    @Schema(description = "出库类型(0-销售出库 1-生产出库 2-调拨出库 3-其他出库)", example = "2")
    private Integer shipmentType;

    @Schema(description = "客户ID", example = "15163")
    private Long customerId;

    @Schema(description = "仓库ID", example = "3588")
    private Long warehouseId;

    @Schema(description = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)", example = "2")
    private Integer orderStatus;

    @Schema(description = "出库状态(0-待出库 1-部分出库 2-已完成)", example = "2")
    private Integer shipmentStatus;

    @Schema(description = "预计出库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] expectTime;

    @Schema(description = "实际完成时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] completeTime;

    @Schema(description = "商品数量", example = "7621")
    private Integer totalCount;

    @Schema(description = "商品金额")
    private BigDecimal totalAmount;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
package cn.smart.wms.module.wms.controller.admin.receiptorder.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 入库单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReceiptOrderPageReqVO extends PageParam {

    @Schema(description = "入库单号")
    private String receiptOrderNo;

    @Schema(description = "入库类型(0-采购入库 1-生产入库 2-退货入库 3-调拨入库)", example = "2")
    private Integer receiptType;

    @Schema(description = "供应商ID", example = "9921")
    private Long supplierId;

    @Schema(description = "仓库ID", example = "24988")
    private Long warehouseId;

    @Schema(description = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)", example = "2")
    private Integer orderStatus;

    @Schema(description = "入库状态(0-待入库 1-部分入库 2-已完成)", example = "2")
    private Integer receiptStatus;

    @Schema(description = "预计到货时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] expectTime;

    @Schema(description = "实际到货时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] arrivalTime;

    @Schema(description = "商品数量", example = "21888")
    private Integer totalCount;

    @Schema(description = "商品金额")
    private BigDecimal totalAmount;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
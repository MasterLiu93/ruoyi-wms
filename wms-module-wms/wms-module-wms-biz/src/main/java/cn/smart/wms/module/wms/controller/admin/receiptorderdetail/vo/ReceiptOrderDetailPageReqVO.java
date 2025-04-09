package cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 入库单明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReceiptOrderDetailPageReqVO extends PageParam {

    @Schema(description = "入库单ID", example = "15781")
    private Long receiptOrderId;

    @Schema(description = "物料ID", example = "30709")
    private Long itemId;

    @Schema(description = "计划数量", example = "30287")
    private Integer planCount;

    @Schema(description = "实际入库数量", example = "23867")
    private Integer realCount;

    @Schema(description = "入库库位ID", example = "12862")
    private Long locationId;

    @Schema(description = "批次ID", example = "25")
    private Long batchId;

    @Schema(description = "入库单价", example = "19948")
    private BigDecimal price;

    @Schema(description = "质检状态(0-未检验 1-已检验)", example = "1")
    private Integer qualityStatus;

    @Schema(description = "状态(0-未入库 1-部分入库 2-已入库)", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
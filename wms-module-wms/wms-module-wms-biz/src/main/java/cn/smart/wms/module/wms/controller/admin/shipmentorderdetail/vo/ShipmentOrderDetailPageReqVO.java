package cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 出库单明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShipmentOrderDetailPageReqVO extends PageParam {

    @Schema(description = "出库单ID", example = "7976")
    private Long shipmentOrderId;

    @Schema(description = "物料ID", example = "636")
    private Long itemId;

    @Schema(description = "计划数量", example = "4797")
    private Integer planCount;

    @Schema(description = "实际出库数量", example = "31306")
    private Integer realCount;

    @Schema(description = "出库库位ID", example = "8986")
    private Long locationId;

    @Schema(description = "批次ID", example = "11127")
    private Long batchId;

    @Schema(description = "出库单价", example = "18864")
    private BigDecimal price;

    @Schema(description = "状态(0-未出库 1-部分出库 2-已出库)", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
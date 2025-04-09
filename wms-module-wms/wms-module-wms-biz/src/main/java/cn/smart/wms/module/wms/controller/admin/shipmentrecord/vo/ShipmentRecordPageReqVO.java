package cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 出库操作记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShipmentRecordPageReqVO extends PageParam {

    @Schema(description = "出库单ID", example = "30700")
    private Long shipmentOrderId;

    @Schema(description = "出库单明细ID", example = "27297")
    private Long shipmentOrderDetailId;

    @Schema(description = "物料ID", example = "2361")
    private Long itemId;

    @Schema(description = "出库库位ID", example = "12101")
    private Long locationId;

    @Schema(description = "批次ID", example = "18521")
    private Long batchId;

    @Schema(description = "出库数量", example = "28862")
    private Integer count;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
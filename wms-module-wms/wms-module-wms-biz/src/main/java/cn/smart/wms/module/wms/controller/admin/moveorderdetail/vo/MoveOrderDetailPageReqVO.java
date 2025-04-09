package cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 移库单明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MoveOrderDetailPageReqVO extends PageParam {

    @Schema(description = "移库单ID", example = "9129")
    private Long moveOrderId;

    @Schema(description = "物料ID", example = "8377")
    private Long itemId;

    @Schema(description = "批次ID", example = "13335")
    private Long batchId;

    @Schema(description = "计划数量", example = "31468")
    private Integer planCount;

    @Schema(description = "实际移库数量", example = "11812")
    private Integer realCount;

    @Schema(description = "状态(0-未移库 1-部分移库 2-已移库)", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
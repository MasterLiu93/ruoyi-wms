package cn.smart.wms.module.wms.controller.admin.moverecord.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 移库操作记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MoveRecordPageReqVO extends PageParam {

    @Schema(description = "移库单ID", example = "2995")
    private Long moveOrderId;

    @Schema(description = "移库单明细ID", example = "16221")
    private Long moveOrderDetailId;

    @Schema(description = "物料ID", example = "321")
    private Long itemId;

    @Schema(description = "批次ID", example = "21284")
    private Long batchId;

    @Schema(description = "源库位ID", example = "28855")
    private Long fromLocationId;

    @Schema(description = "目标库位ID", example = "3389")
    private Long toLocationId;

    @Schema(description = "移动数量", example = "3815")
    private Integer count;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
package cn.smart.wms.module.wms.controller.admin.batchrecord.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 批次操作记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchRecordPageReqVO extends PageParam {

    @Schema(description = "批次ID", example = "26244")
    private Long batchId;

    @Schema(description = "移动类型(0-入库 1-出库 2-库存调整)", example = "2")
    private Integer movementType;

    @Schema(description = "移动记录ID", example = "6602")
    private Long movementId;

    @Schema(description = "操作数量", example = "18886")
    private Integer count;

    @Schema(description = "操作前数量", example = "31616")
    private Integer beforeCount;

    @Schema(description = "操作后数量", example = "6973")
    private Integer afterCount;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
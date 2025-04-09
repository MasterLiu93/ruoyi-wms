package cn.smart.wms.module.wms.controller.admin.batchinventory.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 批次库存分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchInventoryPageReqVO extends PageParam {

    @Schema(description = "批次ID", example = "20347")
    private Long batchId;

    @Schema(description = "物料ID", example = "27051")
    private Long itemId;

    @Schema(description = "仓库ID", example = "2731")
    private Long warehouseId;

    @Schema(description = "库位ID", example = "16585")
    private Long locationId;

    @Schema(description = "库存数量", example = "1177")
    private Integer stockCount;

    @Schema(description = "可用数量", example = "326")
    private Integer availableCount;

    @Schema(description = "锁定数量", example = "30985")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-冻结)", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
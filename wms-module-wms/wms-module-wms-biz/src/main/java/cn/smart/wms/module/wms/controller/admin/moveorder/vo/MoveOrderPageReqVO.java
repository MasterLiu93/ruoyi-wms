package cn.smart.wms.module.wms.controller.admin.moveorder.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 移库单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MoveOrderPageReqVO extends PageParam {

    @Schema(description = "移库单号")
    private String moveOrderNo;

    @Schema(description = "移库类型(0-仓库间移库 1-库区间移库 2-库位间移库)", example = "1")
    private Integer moveType;

    @Schema(description = "源仓库ID", example = "12260")
    private Long fromWarehouseId;

    @Schema(description = "目标仓库ID", example = "9332")
    private Long toWarehouseId;

    @Schema(description = "源库位ID", example = "18298")
    private Long fromLocationId;

    @Schema(description = "目标库位ID", example = "10638")
    private Long toLocationId;

    @Schema(description = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)", example = "2")
    private Integer orderStatus;

    @Schema(description = "移库状态(0-待移库 1-部分移库 2-已完成)", example = "1")
    private Integer moveStatus;

    @Schema(description = "商品数量", example = "2244")
    private Integer totalCount;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
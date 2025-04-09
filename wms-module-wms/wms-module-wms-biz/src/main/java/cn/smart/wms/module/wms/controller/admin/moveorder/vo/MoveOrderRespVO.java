package cn.smart.wms.module.wms.controller.admin.moveorder.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 移库单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MoveOrderRespVO {

    @Schema(description = "移库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30146")
    @ExcelProperty("移库单ID")
    private Long id;

    @Schema(description = "移库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("移库单号")
    private String moveOrderNo;

    @Schema(description = "移库类型(0-仓库间移库 1-库区间移库 2-库位间移库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("移库类型(0-仓库间移库 1-库区间移库 2-库位间移库)")
    private Integer moveType;

    @Schema(description = "源仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12260")
    @ExcelProperty("源仓库ID")
    private Long fromWarehouseId;

    @Schema(description = "目标仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9332")
    @ExcelProperty("目标仓库ID")
    private Long toWarehouseId;

    @Schema(description = "源库位ID", example = "18298")
    @ExcelProperty("源库位ID")
    private Long fromLocationId;

    @Schema(description = "目标库位ID", example = "10638")
    @ExcelProperty("目标库位ID")
    private Long toLocationId;

    @Schema(description = "单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("单据状态(0-草稿 1-待审核 2-审核通过 3-审核拒绝)")
    private Integer orderStatus;

    @Schema(description = "移库状态(0-待移库 1-部分移库 2-已完成)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("移库状态(0-待移库 1-部分移库 2-已完成)")
    private Integer moveStatus;

    @Schema(description = "商品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2244")
    @ExcelProperty("商品数量")
    private Integer totalCount;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
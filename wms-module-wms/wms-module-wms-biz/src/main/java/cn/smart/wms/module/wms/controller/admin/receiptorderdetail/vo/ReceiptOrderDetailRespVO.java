package cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 入库单明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ReceiptOrderDetailRespVO {

    @Schema(description = "入库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11356")
    @ExcelProperty("入库单明细ID")
    private Long id;

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15781")
    @ExcelProperty("入库单ID")
    private Long receiptOrderId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30709")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "物料编码")
    @ExcelProperty("物料编码")
    private String itemCode;

    @Schema(description = "物料名称")
    @ExcelProperty("物料名称")
    private String itemName;

    @Schema(description = "规格型号")
    @ExcelProperty("规格型号")
    private String spec;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String unit;

    @Schema(description = "计划数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "30287")
    @ExcelProperty("计划数量")
    private Integer planCount;

    @Schema(description = "实际入库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "23867")
    @ExcelProperty("实际入库数量")
    private Integer realCount;

    @Schema(description = "货区ID", example = "1001")
    @ExcelProperty("货区ID")
    private Long areaId;

    @Schema(description = "货区名称")
    @ExcelProperty("货区名称")
    private String areaName;

    @Schema(description = "货架ID", example = "2001")
    @ExcelProperty("货架ID")
    private Long rackId;

    @Schema(description = "货架名称")
    @ExcelProperty("货架名称")
    private String rackName;

    @Schema(description = "入库库位ID", example = "12862")
    @ExcelProperty("入库库位ID")
    private Long locationId;

    @Schema(description = "库位名称")
    @ExcelProperty("库位名称")
    private String locationName;

    @Schema(description = "批次ID", example = "25")
    @ExcelProperty("批次ID")
    private Long batchId;

    @Schema(description = "入库单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "19948")
    @ExcelProperty("入库单价")
    private BigDecimal price;

    @Schema(description = "质检状态(0-未检验 1-已检验)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("质检状态(0-未检验 1-已检验)")
    private Integer qualityStatus;

    @Schema(description = "状态(0-未入库 1-部分入库 2-已入库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态(0-未入库 1-部分入库 2-已入库)")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
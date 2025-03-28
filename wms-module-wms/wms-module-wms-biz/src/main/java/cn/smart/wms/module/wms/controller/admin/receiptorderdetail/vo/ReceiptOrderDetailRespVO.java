package cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

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

    @Schema(description = "计划数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "30287")
    @ExcelProperty("计划数量")
    private Integer planCount;

    @Schema(description = "实际入库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "23867")
    @ExcelProperty("实际入库数量")
    private Integer realCount;

    @Schema(description = "入库库位ID", example = "12862")
    @ExcelProperty("入库库位ID")
    private Long locationId;

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
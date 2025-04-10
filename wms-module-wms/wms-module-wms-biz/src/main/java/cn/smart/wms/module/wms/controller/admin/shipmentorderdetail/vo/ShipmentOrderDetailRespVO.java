package cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 出库单明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShipmentOrderDetailRespVO {

    @Schema(description = "出库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28505")
    @ExcelProperty("出库单明细ID")
    private Long id;

    @Schema(description = "出库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7976")
    @ExcelProperty("出库单ID")
    private Long shipmentOrderId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "636")
    @ExcelProperty("物料ID")
    private Long itemId;
    
    @Schema(description = "物料编码")
    @ExcelProperty("物料编码")
    private String itemCode;
    
    @Schema(description = "物料名称")
    @ExcelProperty("物料名称")
    private String itemName;
    
    @Schema(description = "物料类型")
    @ExcelProperty("物料类型")
    private Integer itemType;
    
    @Schema(description = "规格")
    @ExcelProperty("规格")
    private String spec;
    
    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String unit;
    
    @Schema(description = "库存数量")
    @ExcelProperty("库存数量")
    private Integer stockCount;

    @Schema(description = "计划数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "4797")
    @ExcelProperty("计划数量")
    private Integer planCount;

    @Schema(description = "实际出库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "31306")
    @ExcelProperty("实际出库数量")
    private Integer realCount;

    @Schema(description = "出库库位ID", example = "8986")
    @ExcelProperty("出库库位ID")
    private Long locationId;

    @Schema(description = "批次ID", example = "11127")
    @ExcelProperty("批次ID")
    private Long batchId;

    @Schema(description = "出库单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "18864")
    @ExcelProperty("出库单价")
    private BigDecimal price;

    @Schema(description = "状态(0-未出库 1-部分出库 2-已出库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态(0-未出库 1-部分出库 2-已出库)")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
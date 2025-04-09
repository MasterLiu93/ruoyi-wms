package cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 入库单明细导入 Excel VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptOrderDetailImportExcelVO {

    @Schema(description = "入库单编号", example = "RK202405280001")
    @ExcelProperty("入库单编号")
    private String receiptOrderNo;
    
    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15781")
    @ExcelProperty("入库单ID")
    @NotNull(message = "入库单ID不能为空")
    private Long receiptOrderId;

    @Schema(description = "物料编码", example = "WL001")
    @ExcelProperty("物料编码")
    private String itemCode;
    
    @Schema(description = "物料名称", example = "螺丝")
    @ExcelProperty("物料名称")
    private String itemName;
    
    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30709")
    @ExcelProperty("物料ID")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "规格型号", example = "M8")
    @ExcelProperty("规格型号")
    private String spec;
    
    @Schema(description = "单位", example = "个")
    @ExcelProperty("单位")
    private String unit;
    
    @Schema(description = "计划数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "30287")
    @ExcelProperty("计划数量")
    @NotNull(message = "计划数量不能为空")
    private Integer planCount;

    @Schema(description = "入库单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "19948")
    @ExcelProperty("入库单价")
    private Double price;

    @Schema(description = "备注", example = "备注信息")
    @ExcelProperty("备注")
    private String remark;

} 
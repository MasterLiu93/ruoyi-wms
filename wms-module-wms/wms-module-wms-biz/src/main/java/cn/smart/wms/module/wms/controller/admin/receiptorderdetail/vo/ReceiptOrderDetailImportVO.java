package cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo;

import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 入库单明细 Excel 导入 VO
 */
@Data
public class ReceiptOrderDetailImportVO {

    @ExcelProperty("序号")
    private Integer index;
    
    @ExcelProperty("物料名称")
    @NotEmpty(message = "物料名称不能为空")
    private String itemName;

    @ExcelProperty("计划数量")
    @NotNull(message = "计划数量不能为空")
    private Integer planCount;
    
    @ExcelProperty("实际入库数量")
    private Integer realCount;

    @ExcelProperty("货区")
    private String areaName;

    @ExcelProperty("货架")
    private String rackName;

    @ExcelProperty("入库库位")
    private String locationName;

    @ExcelProperty("入库单价")
    private BigDecimal price;
    
    @ExcelProperty("质检状态")
    private Integer qualityStatus;
    
    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("规格")
    private String spec;

    @ExcelProperty("单位")
    private String unit;

    @ExcelProperty("备注")
    private String remark;
} 
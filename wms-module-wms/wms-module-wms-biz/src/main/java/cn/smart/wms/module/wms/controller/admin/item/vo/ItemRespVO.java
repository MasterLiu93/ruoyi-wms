package cn.smart.wms.module.wms.controller.admin.item.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ItemRespVO {

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20482")
    @ExcelProperty("物料ID")
    private Long id;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编码")
    private String itemCode;

    @Schema(description = "物料名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("物料名称")
    private String itemName;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2492")
    @ExcelProperty("分类ID")
    private Long categoryId;

    @Schema(description = "物料类型(0-原材料 1-半成品 2-成品 3-包装材料)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("物料类型(0-原材料 1-半成品 2-成品 3-包装材料)")
    private Integer itemType;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("单位")
    private String unit;

    @Schema(description = "规格")
    @ExcelProperty("规格")
    private String spec;

    @Schema(description = "参考价格", example = "17476")
    @ExcelProperty("参考价格")
    private BigDecimal price;

    @Schema(description = "安全库存")
    @ExcelProperty("安全库存")
    private Integer safetyStock;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态(0-正常 1-禁用)")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
package cn.smart.wms.module.wms.controller.admin.item.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 物料新增/修改 Request VO")
@Data
public class ItemSaveReqVO {

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20482")
    private Long id;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编码不能为空")
    private String itemCode;

    @Schema(description = "物料名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "物料名称不能为空")
    private String itemName;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2492")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @Schema(description = "物料类型(0-原材料 1-半成品 2-成品 3-包装材料)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "物料类型(0-原材料 1-半成品 2-成品 3-包装材料)不能为空")
    private Integer itemType;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "单位不能为空")
    private String unit;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "参考价格", example = "17476")
    private BigDecimal price;

    @Schema(description = "安全库存")
    private Integer safetyStock;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-正常 1-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}
package cn.smart.wms.module.wms.controller.admin.inventorycheck.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 库存盘点结果提交 Request VO")
@Data
public class InventoryCheckResultReqVO {

    @Schema(description = "盘点明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "盘点明细ID不能为空")
    private Long detailId;

    @Schema(description = "盘点数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "盘点数量不能为空")
    @Min(value = 0, message = "盘点数量必须大于等于0")
    private Integer checkCount;

    @Schema(description = "备注", example = "货架上有额外10箱")
    private String remark;
} 
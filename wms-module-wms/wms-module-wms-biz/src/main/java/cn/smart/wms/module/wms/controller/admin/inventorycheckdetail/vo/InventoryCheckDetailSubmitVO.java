package cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 管理后台 - 盘点明细提交 Request VO
 */
@Schema(description = "管理后台 - 盘点明细提交 Request VO")
@Data
public class InventoryCheckDetailSubmitVO {

    @Schema(description = "盘点明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotNull(message = "盘点明细ID不能为空")
    private Long detailId;

    @Schema(description = "盘点数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "盘点数量不能为空")
    @Min(value = 0, message = "盘点数量必须大于等于0")
    private Integer checkCount;

    @Schema(description = "备注", example = "货架A区左侧")
    private String remark;
} 
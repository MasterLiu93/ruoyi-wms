package cn.smart.wms.module.wms.controller.admin.inventorycheck.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 库存盘点差异调整 Request VO")
@Data
public class InventoryCheckAdjustReqVO {

    @Schema(description = "盘点单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "盘点单ID不能为空")
    private Long id;

    @Schema(description = "备注", example = "系统自动调整")
    private String remark;
} 
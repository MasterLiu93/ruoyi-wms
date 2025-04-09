package cn.smart.wms.module.wms.controller.admin.inventorycheck.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 库存盘点批量结果提交 Request VO")
@Data
public class InventoryCheckBatchResultReqVO {

    @Schema(description = "盘点单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "盘点单ID不能为空")
    private Long checkId;

    @Schema(description = "盘点明细列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "盘点明细列表不能为空")
    @Valid
    private List<InventoryCheckDetailSubmitVO> checkDetails;
} 
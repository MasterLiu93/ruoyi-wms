package cn.smart.wms.module.wms.controller.admin.batchinventory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 批次库存创建/修改 Request VO")
@Data
public class BatchInventorySaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "批次编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "批次编号不能为空")
    private Long batchId;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "物料编号不能为空")
    private Long itemId;

    @Schema(description = "仓库编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "仓库编号不能为空")
    private Long warehouseId;

    @Schema(description = "库位编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3072")
    @NotNull(message = "库位编号不能为空")
    private Long locationId;

    @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "库存数量不能为空")
    private Integer stockCount;

    @Schema(description = "锁定库存数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "锁定库存数量不能为空")
    private Integer lockedCount;

    @Schema(description = "可用库存数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "80")
    @NotNull(message = "可用库存数量不能为空")
    private Integer availableCount;

} 
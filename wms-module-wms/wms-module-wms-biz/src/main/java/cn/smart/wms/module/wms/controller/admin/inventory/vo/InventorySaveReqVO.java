package cn.smart.wms.module.wms.controller.admin.inventory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 库存新增/修改 Request VO")
@Data
public class InventorySaveReqVO {

    @Schema(description = "库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6004")
    private Long id;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24551")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15018")
    @NotNull(message = "库位ID不能为空")
    private Long locationId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15331")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "24401")
    @NotNull(message = "库存数量不能为空")
    private Integer stockCount;

    @Schema(description = "可用数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "14232")
    @NotNull(message = "可用数量不能为空")
    private Integer availableCount;

    @Schema(description = "锁定数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "5102")
    @NotNull(message = "锁定数量不能为空")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-正常 1-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}
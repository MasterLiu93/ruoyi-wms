package cn.smart.wms.module.wms.controller.admin.inventorymovement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 库存移动记录新增/修改 Request VO")
@Data
public class InventoryMovementSaveReqVO {

    @Schema(description = "移动ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15739")
    private Long id;

    @Schema(description = "移动类型(0-入库 1-出库 2-库存调整)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "移动类型(0-入库 1-出库 2-库存调整)不能为空")
    private Integer movementType;

    @Schema(description = "移动单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "移动单号不能为空")
    private String movementNo;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11381")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9215")
    @NotNull(message = "库位ID不能为空")
    private Long locationId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14832")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "移动数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2819")
    @NotNull(message = "移动数量不能为空")
    private Integer count;

    @Schema(description = "移动前数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "19001")
    @NotNull(message = "移动前数量不能为空")
    private Integer beforeCount;

    @Schema(description = "移动后数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "4870")
    @NotNull(message = "移动后数量不能为空")
    private Integer afterCount;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "业务类型不能为空")
    private String businessType;

    @Schema(description = "业务单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1117")
    @NotNull(message = "业务单ID不能为空")
    private Long businessId;

    @Schema(description = "业务明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20164")
    @NotNull(message = "业务明细ID不能为空")
    private Long businessDetailId;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
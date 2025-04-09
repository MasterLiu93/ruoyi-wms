package cn.smart.wms.module.wms.controller.admin.moverecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 移库记录创建/更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MoveRecordSaveReqVO extends MoveRecordBaseVO {

    @Schema(description = "移库记录编号", example = "123")
    private Long id;

}

/**
 * 通用移库记录基础对象
 */
@Schema(description = "管理后台 - 移库记录基础对象 VO")
@Data
class MoveRecordBaseVO {

    @Schema(description = "移库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "移库单ID不能为空")
    private Long moveOrderId;

    @Schema(description = "移库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "移库单明细ID不能为空")
    private Long moveOrderDetailId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "批次ID", example = "2048")
    private Long batchId;

    @Schema(description = "源仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "源仓库ID不能为空")
    private Long fromWarehouseId;

    @Schema(description = "目标仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "目标仓库ID不能为空")
    private Long toWarehouseId;

    @Schema(description = "源库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "源库位ID不能为空")
    private Long fromLocationId;

    @Schema(description = "目标库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "目标库位ID不能为空")
    private Long toLocationId;

    @Schema(description = "移库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "移库数量不能为空")
    private Integer count;

    @Schema(description = "备注", example = "正常移库")
    private String remark;

}
package cn.smart.wms.module.wms.controller.admin.moverecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 移库操作记录新增/修改 Request VO")
@Data
public class MoveRecordSaveReqVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    private Long id;

    @Schema(description = "移库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2995")
    @NotNull(message = "移库单ID不能为空")
    private Long moveOrderId;

    @Schema(description = "移库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16221")
    @NotNull(message = "移库单明细ID不能为空")
    private Long moveOrderDetailId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "321")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "批次ID", example = "21284")
    private Long batchId;

    @Schema(description = "源库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28855")
    @NotNull(message = "源库位ID不能为空")
    private Long fromLocationId;

    @Schema(description = "目标库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3389")
    @NotNull(message = "目标库位ID不能为空")
    private Long toLocationId;

    @Schema(description = "移动数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "3815")
    @NotNull(message = "移动数量不能为空")
    private Integer count;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}
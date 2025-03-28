package cn.smart.wms.module.wms.controller.admin.batchinventory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 批次库存新增/修改 Request VO")
@Data
public class BatchInventorySaveReqVO {

    @Schema(description = "批次库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22219")
    private Long id;

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20347")
    @NotNull(message = "批次ID不能为空")
    private Long batchId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27051")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2731")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16585")
    @NotNull(message = "库位ID不能为空")
    private Long locationId;

    @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1177")
    @NotNull(message = "库存数量不能为空")
    private Integer stockCount;

    @Schema(description = "可用数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "326")
    @NotNull(message = "可用数量不能为空")
    private Integer availableCount;

    @Schema(description = "锁定数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "30985")
    @NotNull(message = "锁定数量不能为空")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-冻结)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态(0-正常 1-冻结)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
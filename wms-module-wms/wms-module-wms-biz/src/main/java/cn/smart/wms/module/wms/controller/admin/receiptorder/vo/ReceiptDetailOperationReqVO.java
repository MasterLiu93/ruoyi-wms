package cn.smart.wms.module.wms.controller.admin.receiptorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;

/**
 * 管理后台 - 入库单明细操作 Request VO
 */
@Schema(description = "管理后台 - 入库单明细操作 Request VO")
@Data
public class ReceiptDetailOperationReqVO {

    @Schema(description = "入库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "入库单明细ID不能为空")
    private Long detailId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "库位ID不能为空")
    private Long locationId;

    @Schema(description = "批次ID", example = "3072")
    private Long batchId;

    @Schema(description = "入库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    @NotNull(message = "入库数量不能为空")
    @Min(value = 1, message = "入库数量必须大于0")
    private Integer count;

    @Schema(description = "备注", example = "正常入库")
    private String remark;
} 
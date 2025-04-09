package cn.smart.wms.module.wms.controller.admin.receiptorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 管理后台 - 入库操作 Request VO
 */
@Schema(description = "管理后台 - 入库操作 Request VO")
@Data
public class ReceiptOperationReqVO {

    @Schema(description = "入库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotNull(message = "入库单明细ID不能为空")
    private Long detailId;

    @Schema(description = "入库库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "456")
    @NotNull(message = "入库库位ID不能为空")
    private Long locationId;

    @Schema(description = "批次ID", example = "789")
    private Long batchId;

    @Schema(description = "入库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "入库数量不能为空")
    @Min(value = 1, message = "入库数量必须大于0")
    private Integer count;

    @Schema(description = "备注", example = "正常入库")
    private String remark;
} 
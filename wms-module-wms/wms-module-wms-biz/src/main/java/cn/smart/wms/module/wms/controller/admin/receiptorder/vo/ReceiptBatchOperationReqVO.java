package cn.smart.wms.module.wms.controller.admin.receiptorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 管理后台 - 批量入库操作 Request VO
 */
@Schema(description = "管理后台 - 批量入库操作 Request VO")
@Data
public class ReceiptBatchOperationReqVO {

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotNull(message = "入库单ID不能为空")
    private Long orderId;

    @Schema(description = "入库明细列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "入库明细不能为空")
    @Valid
    private List<ReceiptOperationReqVO> details;
} 
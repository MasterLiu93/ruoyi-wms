package cn.smart.wms.module.wms.controller.admin.receiptorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 管理后台 - 入库单审核 Request VO
 */
@Schema(description = "管理后台 - 入库单审核 Request VO")
@Data
public class ReceiptOrderApproveReqVO {

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "入库单ID不能为空")
    private Long id;

    @Schema(description = "是否审核通过", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否审核通过不能为空")
    private Boolean approved;

    @Schema(description = "备注", example = "审核通过")
    private String remark;
} 
package cn.smart.wms.module.wms.controller.admin.receiptorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 管理后台 - 入库单取消 Request VO
 */
@Schema(description = "管理后台 - 入库单取消 Request VO")
@Data
public class ReceiptOrderCancelReqVO {

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotNull(message = "入库单ID不能为空")
    private Long id;

    @Schema(description = "取消原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "供应商延期交货")
    @NotNull(message = "取消原因不能为空")
    private String remark;
} 
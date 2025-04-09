package cn.smart.wms.module.wms.controller.admin.moveorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 管理后台 - 移库操作 Request VO
 */
@Schema(description = "管理后台 - 移库操作 Request VO")
@Data
public class MoveOperationReqVO {

    @Schema(description = "移库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotNull(message = "移库单明细ID不能为空")
    private Long detailId;

    @Schema(description = "移库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "移库数量不能为空")
    @Min(value = 1, message = "移库数量必须大于0")
    private Integer count;

    @Schema(description = "备注", example = "正常移库")
    private String remark;
} 
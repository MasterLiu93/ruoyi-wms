package cn.smart.wms.module.wms.controller.admin.rack.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 货架新增/修改 Request VO")
@Data
public class RackSaveReqVO {

    @Schema(description = "货架ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21002")
    private Long id;

    @Schema(description = "货架编码")
    private String rackCode;

    @Schema(description = "货架名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "货架名称不能为空")
    private String rackName;

    @Schema(description = "所属货区ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29159")
    @NotNull(message = "所属货区ID不能为空")
    private Long areaId;

    @Schema(description = "货架类型(0-标准货架 1-重型货架 2-悬臂货架)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "货架类型(0-标准货架 1-重型货架 2-悬臂货架)不能为空")
    private Integer rackType;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态(0-正常 1-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
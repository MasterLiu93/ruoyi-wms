package cn.smart.wms.module.wms.controller.admin.area.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 货区新增/修改 Request VO")
@Data
public class AreaSaveReqVO {

    @Schema(description = "货区ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8262")
    private Long id;

    @Schema(description = "货区编码")
    private String areaCode;

    @Schema(description = "货区名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "货区名称不能为空")
    private String areaName;

    @Schema(description = "所属仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27526")
    @NotNull(message = "所属仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "货区类型(0-存储区 1-暂存区 2-拣货区 3-出货区)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "货区类型(0-存储区 1-暂存区 2-拣货区 3-出货区)不能为空")
    private Integer areaType;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-正常 1-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
package cn.smart.wms.module.wms.controller.admin.location.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 库位新增/修改 Request VO")
@Data
public class LocationSaveReqVO {

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1201")
    private Long id;

    @Schema(description = "库位编码")
    private String locationCode;

    @Schema(description = "库位名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "库位名称不能为空")
    private String locationName;

    @Schema(description = "所属货架ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29106")
    @NotNull(message = "所属货架ID不能为空")
    private Long rackId;

    @Schema(description = "库位类型(0-普通 1-快检 2-退货)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "库位类型(0-普通 1-快检 2-退货)不能为空")
    private Integer locationType;

    @Schema(description = "状态(0-空闲 1-占用 2-锁定 3-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-空闲 1-占用 2-锁定 3-禁用)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
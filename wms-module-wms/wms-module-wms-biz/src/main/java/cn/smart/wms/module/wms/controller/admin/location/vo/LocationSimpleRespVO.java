package cn.smart.wms.module.wms.controller.admin.location.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 库位精简信息 Response VO")
@Data
public class LocationSimpleRespVO {

    @Schema(description = "库位编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "库位编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "A-01-01")
    private String locationCode;

    @Schema(description = "库位名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "A区1排1列")
    private String locationName;

    @Schema(description = "货架编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long rackId;
} 
package cn.smart.wms.module.wms.controller.admin.warehouse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 仓库精简信息 Response VO")
@Data
public class WarehouseSimpleRespVO {

    @Schema(description = "仓库编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "原材料仓库")
    private String name;

    @Schema(description = "仓库编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "WH-001")
    private String code;

} 
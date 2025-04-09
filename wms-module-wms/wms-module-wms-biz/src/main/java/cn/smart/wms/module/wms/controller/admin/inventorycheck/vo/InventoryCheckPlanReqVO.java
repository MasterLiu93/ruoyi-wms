package cn.smart.wms.module.wms.controller.admin.inventorycheck.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 库存盘点计划创建 Request VO")
@Data
public class InventoryCheckPlanReqVO {

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "盘点类型(0-全部盘点 1-部分盘点)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "盘点类型不能为空")
    private Integer checkType;

    @Schema(description = "库位ID列表，当盘点类型为部分盘点且按库位盘点时使用", example = "[1024, 2048]")
    private List<Long> locationIds;

    @Schema(description = "物料ID列表，当盘点类型为部分盘点且按物料盘点时使用", example = "[1024, 2048]")
    private List<Long> itemIds;

    @Schema(description = "备注", example = "月度盘点")
    private String remark;
} 
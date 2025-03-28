package cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 库存盘点明细新增/修改 Request VO")
@Data
public class InventoryCheckDetailSaveReqVO {

    @Schema(description = "盘点明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16467")
    private Long id;

    @Schema(description = "盘点单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23284")
    @NotNull(message = "盘点单ID不能为空")
    private Long checkId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6328")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14363")
    @NotNull(message = "库位ID不能为空")
    private Long locationId;

    @Schema(description = "账面数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "27620")
    @NotNull(message = "账面数量不能为空")
    private Integer bookCount;

    @Schema(description = "盘点数量", example = "26450")
    private Integer checkCount;

    @Schema(description = "差异数量", example = "30553")
    private Integer differenceCount;

    @Schema(description = "盘点状态(0-未盘点 1-已盘点)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "盘点状态(0-未盘点 1-已盘点)不能为空")
    private Integer checkStatus;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}
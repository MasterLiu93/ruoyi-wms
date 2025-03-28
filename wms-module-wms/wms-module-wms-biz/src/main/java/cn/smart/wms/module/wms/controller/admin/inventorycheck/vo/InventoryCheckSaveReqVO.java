package cn.smart.wms.module.wms.controller.admin.inventorycheck.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 库存盘点单新增/修改 Request VO")
@Data
public class InventoryCheckSaveReqVO {

    @Schema(description = "盘点单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12330")
    private Long id;

    @Schema(description = "盘点单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "盘点单号不能为空")
    private String checkNo;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1135")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    @Schema(description = "盘点类型(0-全部盘点 1-部分盘点)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "盘点类型(0-全部盘点 1-部分盘点)不能为空")
    private Integer checkType;

    @Schema(description = "盘点状态(0-新建 1-盘点中 2-已完成)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "盘点状态(0-新建 1-盘点中 2-已完成)不能为空")
    private Integer checkStatus;

    @Schema(description = "盘点总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "13748")
    @NotNull(message = "盘点总数不能为空")
    private Integer totalCount;

    @Schema(description = "已盘点数", requiredMode = Schema.RequiredMode.REQUIRED, example = "8727")
    @NotNull(message = "已盘点数不能为空")
    private Integer checkedCount;

    @Schema(description = "差异数", requiredMode = Schema.RequiredMode.REQUIRED, example = "32025")
    @NotNull(message = "差异数不能为空")
    private Integer differenceCount;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}
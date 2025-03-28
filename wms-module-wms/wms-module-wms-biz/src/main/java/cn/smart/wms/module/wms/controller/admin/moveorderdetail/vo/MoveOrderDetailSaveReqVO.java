package cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 移库单明细新增/修改 Request VO")
@Data
public class MoveOrderDetailSaveReqVO {

    @Schema(description = "移库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11264")
    private Long id;

    @Schema(description = "移库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9129")
    @NotNull(message = "移库单ID不能为空")
    private Long moveOrderId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8377")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "批次ID", example = "13335")
    private Long batchId;

    @Schema(description = "计划数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "31468")
    @NotNull(message = "计划数量不能为空")
    private Integer planCount;

    @Schema(description = "实际移库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "11812")
    @NotNull(message = "实际移库数量不能为空")
    private Integer realCount;

    @Schema(description = "状态(0-未移库 1-部分移库 2-已移库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-未移库 1-部分移库 2-已移库)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}
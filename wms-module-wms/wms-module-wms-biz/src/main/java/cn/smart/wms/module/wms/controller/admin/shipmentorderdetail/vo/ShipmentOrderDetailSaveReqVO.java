package cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 出库单明细新增/修改 Request VO")
@Data
public class ShipmentOrderDetailSaveReqVO {

    @Schema(description = "出库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28505")
    private Long id;

    @Schema(description = "出库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7976")
    @NotNull(message = "出库单ID不能为空")
    private Long shipmentOrderId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "636")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "计划数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "4797")
    @NotNull(message = "计划数量不能为空")
    private Integer planCount;

    @Schema(description = "实际出库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "31306")
    @NotNull(message = "实际出库数量不能为空")
    private Integer realCount;

    @Schema(description = "出库库位ID", example = "8986")
    private Long locationId;

    @Schema(description = "批次ID", example = "11127")
    private Long batchId;

    @Schema(description = "出库单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "18864")
    @NotNull(message = "出库单价不能为空")
    private BigDecimal price;

    @Schema(description = "状态(0-未出库 1-部分出库 2-已出库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态(0-未出库 1-部分出库 2-已出库)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}
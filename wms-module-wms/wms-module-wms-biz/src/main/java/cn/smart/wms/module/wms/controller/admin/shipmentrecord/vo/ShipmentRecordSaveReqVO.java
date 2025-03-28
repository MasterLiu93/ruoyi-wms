package cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 出库操作记录新增/修改 Request VO")
@Data
public class ShipmentRecordSaveReqVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18538")
    private Long id;

    @Schema(description = "出库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30700")
    @NotNull(message = "出库单ID不能为空")
    private Long shipmentOrderId;

    @Schema(description = "出库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27297")
    @NotNull(message = "出库单明细ID不能为空")
    private Long shipmentOrderDetailId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2361")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "出库库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12101")
    @NotNull(message = "出库库位ID不能为空")
    private Long locationId;

    @Schema(description = "批次ID", example = "18521")
    private Long batchId;

    @Schema(description = "出库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "28862")
    @NotNull(message = "出库数量不能为空")
    private Integer count;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}
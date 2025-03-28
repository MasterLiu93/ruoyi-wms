package cn.smart.wms.module.wms.controller.admin.receiptorderdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 入库单明细新增/修改 Request VO")
@Data
public class ReceiptOrderDetailSaveReqVO {

    @Schema(description = "入库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11356")
    private Long id;

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15781")
    @NotNull(message = "入库单ID不能为空")
    private Long receiptOrderId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30709")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "计划数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "30287")
    @NotNull(message = "计划数量不能为空")
    private Integer planCount;

    @Schema(description = "实际入库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "23867")
    @NotNull(message = "实际入库数量不能为空")
    private Integer realCount;

    @Schema(description = "入库库位ID", example = "12862")
    private Long locationId;

    @Schema(description = "批次ID", example = "25")
    private Long batchId;

    @Schema(description = "入库单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "19948")
    @NotNull(message = "入库单价不能为空")
    private BigDecimal price;

    @Schema(description = "质检状态(0-未检验 1-已检验)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "质检状态(0-未检验 1-已检验)不能为空")
    private Integer qualityStatus;

    @Schema(description = "状态(0-未入库 1-部分入库 2-已入库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态(0-未入库 1-部分入库 2-已入库)不能为空")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}
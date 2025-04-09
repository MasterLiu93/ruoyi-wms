package cn.smart.wms.module.wms.controller.admin.batchrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 批次操作记录新增/修改 Request VO")
@Data
public class BatchRecordSaveReqVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8753")
    private Long id;

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26244")
    @NotNull(message = "批次ID不能为空")
    private Long batchId;

    @Schema(description = "移动类型(0-入库 1-出库 2-库存调整)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "移动类型(0-入库 1-出库 2-库存调整)不能为空")
    private Integer movementType;

    @Schema(description = "移动记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6602")
    @NotNull(message = "移动记录ID不能为空")
    private Long movementId;

    @Schema(description = "操作数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "18886")
    @NotNull(message = "操作数量不能为空")
    private Integer count;

    @Schema(description = "操作前数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "31616")
    @NotNull(message = "操作前数量不能为空")
    private Integer beforeCount;

    @Schema(description = "操作后数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "6973")
    @NotNull(message = "操作后数量不能为空")
    private Integer afterCount;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}
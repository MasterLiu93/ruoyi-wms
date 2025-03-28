package cn.smart.wms.module.wms.controller.admin.receiptrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 入库操作记录新增/修改 Request VO")
@Data
public class ReceiptRecordSaveReqVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "307")
    private Long id;

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14146")
    @NotNull(message = "入库单ID不能为空")
    private Long receiptOrderId;

    @Schema(description = "入库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9171")
    @NotNull(message = "入库单明细ID不能为空")
    private Long receiptOrderDetailId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "561")
    @NotNull(message = "物料ID不能为空")
    private Long itemId;

    @Schema(description = "入库库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19637")
    @NotNull(message = "入库库位ID不能为空")
    private Long locationId;

    @Schema(description = "批次ID", example = "4382")
    private Long batchId;

    @Schema(description = "入库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "49")
    @NotNull(message = "入库数量不能为空")
    private Integer count;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}
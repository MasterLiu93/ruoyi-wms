package cn.smart.wms.module.wms.controller.admin.receiptrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 入库操作记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ReceiptRecordRespVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "307")
    @ExcelProperty("记录ID")
    private Long id;

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14146")
    @ExcelProperty("入库单ID")
    private Long receiptOrderId;

    @Schema(description = "入库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9171")
    @ExcelProperty("入库单明细ID")
    private Long receiptOrderDetailId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "561")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "入库库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19637")
    @ExcelProperty("入库库位ID")
    private Long locationId;

    @Schema(description = "批次ID", example = "4382")
    @ExcelProperty("批次ID")
    private Long batchId;

    @Schema(description = "入库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "49")
    @ExcelProperty("入库数量")
    private Integer count;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
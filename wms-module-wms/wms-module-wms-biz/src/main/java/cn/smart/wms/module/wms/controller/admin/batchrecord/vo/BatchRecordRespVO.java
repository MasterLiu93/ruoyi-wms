package cn.smart.wms.module.wms.controller.admin.batchrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 批次操作记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BatchRecordRespVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8753")
    @ExcelProperty("记录ID")
    private Long id;

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26244")
    @ExcelProperty("批次ID")
    private Long batchId;

    @Schema(description = "移动类型(0-入库 1-出库 2-库存调整)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("移动类型(0-入库 1-出库 2-库存调整)")
    private Integer movementType;

    @Schema(description = "移动记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6602")
    @ExcelProperty("移动记录ID")
    private Long movementId;

    @Schema(description = "操作数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "18886")
    @ExcelProperty("操作数量")
    private Integer count;

    @Schema(description = "操作前数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "31616")
    @ExcelProperty("操作前数量")
    private Integer beforeCount;

    @Schema(description = "操作后数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "6973")
    @ExcelProperty("操作后数量")
    private Integer afterCount;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
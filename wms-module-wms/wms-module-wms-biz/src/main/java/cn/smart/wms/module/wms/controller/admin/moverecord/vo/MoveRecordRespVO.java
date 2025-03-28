package cn.smart.wms.module.wms.controller.admin.moverecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 移库操作记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MoveRecordRespVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12")
    @ExcelProperty("记录ID")
    private Long id;

    @Schema(description = "移库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2995")
    @ExcelProperty("移库单ID")
    private Long moveOrderId;

    @Schema(description = "移库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16221")
    @ExcelProperty("移库单明细ID")
    private Long moveOrderDetailId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "321")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "批次ID", example = "21284")
    @ExcelProperty("批次ID")
    private Long batchId;

    @Schema(description = "源库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28855")
    @ExcelProperty("源库位ID")
    private Long fromLocationId;

    @Schema(description = "目标库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3389")
    @ExcelProperty("目标库位ID")
    private Long toLocationId;

    @Schema(description = "移动数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "3815")
    @ExcelProperty("移动数量")
    private Integer count;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
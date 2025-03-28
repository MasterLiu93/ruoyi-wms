package cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 移库单明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MoveOrderDetailRespVO {

    @Schema(description = "移库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11264")
    @ExcelProperty("移库单明细ID")
    private Long id;

    @Schema(description = "移库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9129")
    @ExcelProperty("移库单ID")
    private Long moveOrderId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8377")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "批次ID", example = "13335")
    @ExcelProperty("批次ID")
    private Long batchId;

    @Schema(description = "计划数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "31468")
    @ExcelProperty("计划数量")
    private Integer planCount;

    @Schema(description = "实际移库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "11812")
    @ExcelProperty("实际移库数量")
    private Integer realCount;

    @Schema(description = "状态(0-未移库 1-部分移库 2-已移库)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态(0-未移库 1-部分移库 2-已移库)")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
package cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 出库操作记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShipmentRecordRespVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18538")
    @ExcelProperty("记录ID")
    private Long id;

    @Schema(description = "出库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30700")
    @ExcelProperty("出库单ID")
    private Long shipmentOrderId;

    @Schema(description = "出库单明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27297")
    @ExcelProperty("出库单明细ID")
    private Long shipmentOrderDetailId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2361")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "出库库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12101")
    @ExcelProperty("出库库位ID")
    private Long locationId;

    @Schema(description = "批次ID", example = "18521")
    @ExcelProperty("批次ID")
    private Long batchId;

    @Schema(description = "出库数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "28862")
    @ExcelProperty("出库数量")
    private Integer count;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
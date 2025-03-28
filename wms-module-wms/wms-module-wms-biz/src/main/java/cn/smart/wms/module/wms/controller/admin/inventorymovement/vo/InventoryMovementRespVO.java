package cn.smart.wms.module.wms.controller.admin.inventorymovement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 库存移动记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InventoryMovementRespVO {

    @Schema(description = "移动ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15739")
    @ExcelProperty("移动ID")
    private Long id;

    @Schema(description = "移动类型(0-入库 1-出库 2-库存调整)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("移动类型(0-入库 1-出库 2-库存调整)")
    private Integer movementType;

    @Schema(description = "移动单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("移动单号")
    private String movementNo;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11381")
    @ExcelProperty("仓库ID")
    private Long warehouseId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9215")
    @ExcelProperty("库位ID")
    private Long locationId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14832")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "移动数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2819")
    @ExcelProperty("移动数量")
    private Integer count;

    @Schema(description = "移动前数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "19001")
    @ExcelProperty("移动前数量")
    private Integer beforeCount;

    @Schema(description = "移动后数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "4870")
    @ExcelProperty("移动后数量")
    private Integer afterCount;

    @Schema(description = "业务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("业务类型")
    private String businessType;

    @Schema(description = "业务单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1117")
    @ExcelProperty("业务单ID")
    private Long businessId;

    @Schema(description = "业务明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20164")
    @ExcelProperty("业务明细ID")
    private Long businessDetailId;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
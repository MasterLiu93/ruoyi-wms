package cn.smart.wms.module.wms.controller.admin.batchinventory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 批次库存 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BatchInventoryRespVO {

    @Schema(description = "批次库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22219")
    @ExcelProperty("批次库存ID")
    private Long id;

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20347")
    @ExcelProperty("批次ID")
    private Long batchId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27051")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2731")
    @ExcelProperty("仓库ID")
    private Long warehouseId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16585")
    @ExcelProperty("库位ID")
    private Long locationId;

    @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1177")
    @ExcelProperty("库存数量")
    private Integer stockCount;

    @Schema(description = "可用数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "326")
    @ExcelProperty("可用数量")
    private Integer availableCount;

    @Schema(description = "锁定数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "30985")
    @ExcelProperty("锁定数量")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-冻结)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态(0-正常 1-冻结)")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
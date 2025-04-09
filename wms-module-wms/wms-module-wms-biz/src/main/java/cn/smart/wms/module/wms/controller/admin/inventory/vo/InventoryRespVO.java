package cn.smart.wms.module.wms.controller.admin.inventory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 库存 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InventoryRespVO {

    @Schema(description = "库存ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6004")
    @ExcelProperty("库存ID")
    private Long id;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24551")
    @ExcelProperty("仓库ID")
    private Long warehouseId;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("仓库名称")
    private String warehouseName;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15018")
    @ExcelProperty("库位ID")
    private Long locationId;

    @Schema(description = "库位名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("库位名称")
    private String locationName;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15331")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "物料名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料名称")
    private String itemName;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编码")
    private String itemCode;

    @Schema(description = "物料类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料类型")
    private Integer itemType;

    @Schema(description = "规格型号")
    @ExcelProperty("规格型号")
    private String spec;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String unit;

    @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "24401")
    @ExcelProperty("库存数量")
    private Integer stockCount;

    @Schema(description = "可用数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "14232")
    @ExcelProperty("可用数量")
    private Integer availableCount;

    @Schema(description = "锁定数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "5102")
    @ExcelProperty("锁定数量")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态(0-正常 1-禁用)")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
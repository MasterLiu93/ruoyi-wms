package cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 库存盘点明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InventoryCheckDetailRespVO {

    @Schema(description = "盘点明细ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16467")
    @ExcelProperty("盘点明细ID")
    private Long id;

    @Schema(description = "盘点单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23284")
    @ExcelProperty("盘点单ID")
    private Long checkId;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6328")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14363")
    @ExcelProperty("库位ID")
    private Long locationId;

    @Schema(description = "账面数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "27620")
    @ExcelProperty("账面数量")
    private Integer bookCount;

    @Schema(description = "盘点数量", example = "26450")
    @ExcelProperty("盘点数量")
    private Integer checkCount;

    @Schema(description = "差异数量", example = "30553")
    @ExcelProperty("差异数量")
    private Integer differenceCount;

    @Schema(description = "盘点状态(0-未盘点 1-已盘点)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("盘点状态(0-未盘点 1-已盘点)")
    private Integer checkStatus;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
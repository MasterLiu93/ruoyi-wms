package cn.smart.wms.module.wms.controller.admin.inventorycheck.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 库存盘点单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InventoryCheckRespVO {

    @Schema(description = "盘点单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12330")
    @ExcelProperty("盘点单ID")
    private Long id;

    @Schema(description = "盘点单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("盘点单号")
    private String checkNo;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1135")
    @ExcelProperty("仓库ID")
    private Long warehouseId;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("仓库名称")
    private String warehouseName;

    @Schema(description = "盘点类型(0-全部盘点 1-部分盘点)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("盘点类型(0-全部盘点 1-部分盘点)")
    private Integer checkType;

    @Schema(description = "盘点状态(0-新建 1-盘点中 2-已完成)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("盘点状态(0-新建 1-盘点中 2-已完成)")
    private Integer checkStatus;

    @Schema(description = "盘点总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "13748")
    @ExcelProperty("盘点总数")
    private Integer totalCount;

    @Schema(description = "已盘点数", requiredMode = Schema.RequiredMode.REQUIRED, example = "8727")
    @ExcelProperty("已盘点数")
    private Integer checkedCount;

    @Schema(description = "差异数", requiredMode = Schema.RequiredMode.REQUIRED, example = "32025")
    @ExcelProperty("差异数")
    private Integer differenceCount;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建人")
    private String creator;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新人")
    private String updater;

}
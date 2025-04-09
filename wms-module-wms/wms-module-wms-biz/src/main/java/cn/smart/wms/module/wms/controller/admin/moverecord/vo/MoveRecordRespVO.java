package cn.smart.wms.module.wms.controller.admin.moverecord.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 移库记录 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MoveRecordRespVO extends MoveRecordBaseVO {

    @Schema(description = "记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @ExcelProperty("记录ID")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "创建人", example = "admin")
    @ExcelProperty("创建人")
    private String creator;
    
    @Schema(description = "移库单号", example = "M2023101001")
    @ExcelProperty("移库单号")
    private String moveOrderNo;
    
    @Schema(description = "物料编码", example = "SKU-001")
    @ExcelProperty("物料编码")
    private String itemCode;
    
    @Schema(description = "物料名称", example = "苹果手机")
    @ExcelProperty("物料名称")
    private String itemName;
    
    @Schema(description = "批次号", example = "B20231001")
    @ExcelProperty("批次号")
    private String batchNo;
    
    @Schema(description = "源仓库名称", example = "主仓库")
    @ExcelProperty("源仓库名称")
    private String fromWarehouseName;
    
    @Schema(description = "目标仓库名称", example = "次仓库")
    @ExcelProperty("目标仓库名称")
    private String toWarehouseName;
    
    @Schema(description = "源库位编码", example = "A-01-01")
    @ExcelProperty("源库位编码")
    private String fromLocationCode;
    
    @Schema(description = "目标库位编码", example = "B-01-01")
    @ExcelProperty("目标库位编码")
    private String toLocationCode;
}
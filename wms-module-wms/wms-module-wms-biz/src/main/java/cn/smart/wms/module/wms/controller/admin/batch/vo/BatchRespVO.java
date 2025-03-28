package cn.smart.wms.module.wms.controller.admin.batch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 批次信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BatchRespVO {

    @Schema(description = "批次ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30442")
    @ExcelProperty("批次ID")
    private Long id;

    @Schema(description = "批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("批次号")
    private String batchNo;

    @Schema(description = "物料ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26918")
    @ExcelProperty("物料ID")
    private Long itemId;

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10449")
    @ExcelProperty("仓库ID")
    private Long warehouseId;

    @Schema(description = "供应商ID", example = "7591")
    @ExcelProperty("供应商ID")
    private Long supplierId;

    @Schema(description = "生产日期")
    @ExcelProperty("生产日期")
    private LocalDate productionDate;

    @Schema(description = "过期日期")
    @ExcelProperty("过期日期")
    private LocalDate expiryDate;

    @Schema(description = "批次属性1")
    @ExcelProperty("批次属性1")
    private String batchAttr1;

    @Schema(description = "批次属性2")
    @ExcelProperty("批次属性2")
    private String batchAttr2;

    @Schema(description = "批次属性3")
    @ExcelProperty("批次属性3")
    private String batchAttr3;

    @Schema(description = "批次属性4")
    @ExcelProperty("批次属性4")
    private String batchAttr4;

    @Schema(description = "入库总数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "7693")
    @ExcelProperty("入库总数量")
    private Integer totalCount;

    @Schema(description = "可用数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "20352")
    @ExcelProperty("可用数量")
    private Integer availableCount;

    @Schema(description = "锁定数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10915")
    @ExcelProperty("锁定数量")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-冻结 2-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态(0-正常 1-冻结 2-禁用)")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
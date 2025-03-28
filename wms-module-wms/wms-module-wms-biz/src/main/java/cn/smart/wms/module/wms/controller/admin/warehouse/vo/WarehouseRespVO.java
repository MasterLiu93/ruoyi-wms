package cn.smart.wms.module.wms.controller.admin.warehouse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 仓库 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WarehouseRespVO {

    @Schema(description = "仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7817")
    @ExcelProperty("仓库ID")
    private Long id;

    @Schema(description = "仓库编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("仓库编码")
    private String warehouseCode;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("仓库名称")
    private String warehouseName;

    @Schema(description = "仓库类型(0-原材料 1-半成品 2-成品 3-退货)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("仓库类型(0-原材料 1-半成品 2-成品 3-退货)")
    private Integer warehouseType;

    @Schema(description = "面积(平方米)")
    @ExcelProperty("面积(平方米)")
    private BigDecimal area;

    @Schema(description = "地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("地址")
    private String address;

    @Schema(description = "负责人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("负责人")
    private String chargePerson;

    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("联系电话")
    private String phone;

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
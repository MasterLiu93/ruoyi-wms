package cn.smart.wms.module.wms.controller.admin.area.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 货区 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AreaRespVO {

    @Schema(description = "货区ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8262")
    @ExcelProperty("货区ID")
    private Long id;

    @Schema(description = "货区编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("货区编码")
    private String areaCode;

    @Schema(description = "货区名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("货区名称")
    private String areaName;

    @Schema(description = "所属仓库ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27526")
    @ExcelProperty("所属仓库ID")
    private Long warehouseId;
    
    @Schema(description = "所属仓库名称")
    private String warehouseName;

    @Schema(description = "货区类型(0-存储区 1-暂存区 2-拣货区 3-出货区)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("货区类型(0-存储区 1-暂存区 2-拣货区 3-出货区)")
    private Integer areaType;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态(0-正常 1-禁用)")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
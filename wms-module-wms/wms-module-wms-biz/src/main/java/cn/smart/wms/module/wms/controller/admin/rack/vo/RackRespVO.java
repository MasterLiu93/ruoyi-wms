package cn.smart.wms.module.wms.controller.admin.rack.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 货架 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RackRespVO {

    @Schema(description = "货架ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21002")
    @ExcelProperty("货架ID")
    private Long id;

    @Schema(description = "货架编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("货架编码")
    private String rackCode;

    @Schema(description = "货架名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("货架名称")
    private String rackName;

    @Schema(description = "所属货区ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29159")
    @ExcelProperty("所属货区ID")
    private Long areaId;

    @Schema(description = "货架类型(0-标准货架 1-重型货架 2-悬臂货架)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("货架类型(0-标准货架 1-重型货架 2-悬臂货架)")
    private Integer rackType;

    @Schema(description = "状态(0-正常 1-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态(0-正常 1-禁用)")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
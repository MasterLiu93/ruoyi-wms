package cn.smart.wms.module.wms.controller.admin.location.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 库位 Response VO")
@Data
@ExcelIgnoreUnannotated
public class LocationRespVO {

    @Schema(description = "库位ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1201")
    @ExcelProperty("库位ID")
    private Long id;

    @Schema(description = "库位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("库位编码")
    private String locationCode;

    @Schema(description = "库位名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("库位名称")
    private String locationName;

    @Schema(description = "所属货架ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29106")
    @ExcelProperty("所属货架ID")
    private Long rackId;

    @Schema(description = "库位类型(0-普通 1-快检 2-退货)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("库位类型(0-普通 1-快检 2-退货)")
    private Integer locationType;

    @Schema(description = "状态(0-空闲 1-占用 2-锁定 3-禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态(0-空闲 1-占用 2-锁定 3-禁用)")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
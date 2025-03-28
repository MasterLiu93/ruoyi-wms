package cn.smart.wms.module.wms.controller.admin.area.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 货区分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AreaPageReqVO extends PageParam {

    @Schema(description = "货区编码")
    private String areaCode;

    @Schema(description = "货区名称", example = "赵六")
    private String areaName;

    @Schema(description = "所属仓库ID", example = "27526")
    private Long warehouseId;

    @Schema(description = "货区类型(0-存储区 1-暂存区 2-拣货区 3-出货区)", example = "2")
    private Integer areaType;

    @Schema(description = "状态(0-正常 1-禁用)", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
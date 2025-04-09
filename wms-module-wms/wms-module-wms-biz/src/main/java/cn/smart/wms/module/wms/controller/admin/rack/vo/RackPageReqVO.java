package cn.smart.wms.module.wms.controller.admin.rack.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 货架分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RackPageReqVO extends PageParam {

    @Schema(description = "货架编码")
    private String rackCode;

    @Schema(description = "货架名称", example = "芋艿")
    private String rackName;

    @Schema(description = "所属货区ID", example = "29159")
    private Long areaId;

    @Schema(description = "货架类型(0-标准货架 1-重型货架 2-悬臂货架)", example = "2")
    private Integer rackType;

    @Schema(description = "状态(0-正常 1-禁用)", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
package cn.smart.wms.module.wms.controller.admin.location.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库位分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LocationPageReqVO extends PageParam {

    @Schema(description = "库位编码")
    private String locationCode;

    @Schema(description = "库位名称", example = "赵六")
    private String locationName;

    @Schema(description = "所属货架ID", example = "29106")
    private Long rackId;

    @Schema(description = "库位类型(0-普通 1-快检 2-退货)", example = "1")
    private Integer locationType;

    @Schema(description = "状态(0-空闲 1-占用 2-锁定 3-禁用)", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
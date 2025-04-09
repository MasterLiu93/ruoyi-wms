package cn.smart.wms.module.wms.controller.admin.location.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库位分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LocationPageReqVO extends PageParam {

    @Schema(description = "仓库编号", example = "1024")
    private Long warehouseId;

    @Schema(description = "货区编号", example = "2048")
    private Long areaId;

    @Schema(description = "货架编号", example = "3072")
    private Long rackId;

    @Schema(description = "库位编号", example = "A01")
    private String locationCode;

    @Schema(description = "库位名称", example = "A区01号")
    private String locationName;

    @Schema(description = "库位类型", example = "1")
    private Integer locationType;

    @Schema(description = "库位状态（0正常 1停用）", example = "0")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
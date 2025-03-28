package cn.smart.wms.module.wms.controller.admin.batch.vo;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 批次信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BatchPageReqVO extends PageParam {

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "物料ID", example = "26918")
    private Long itemId;

    @Schema(description = "仓库ID", example = "10449")
    private Long warehouseId;

    @Schema(description = "供应商ID", example = "7591")
    private Long supplierId;

    @Schema(description = "生产日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] productionDate;

    @Schema(description = "过期日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] expiryDate;

    @Schema(description = "批次属性1")
    private String batchAttr1;

    @Schema(description = "批次属性2")
    private String batchAttr2;

    @Schema(description = "批次属性3")
    private String batchAttr3;

    @Schema(description = "批次属性4")
    private String batchAttr4;

    @Schema(description = "入库总数量", example = "7693")
    private Integer totalCount;

    @Schema(description = "可用数量", example = "20352")
    private Integer availableCount;

    @Schema(description = "锁定数量", example = "10915")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-冻结 2-禁用)", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
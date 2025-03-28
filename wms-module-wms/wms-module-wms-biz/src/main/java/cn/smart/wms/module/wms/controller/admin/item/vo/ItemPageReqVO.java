package cn.smart.wms.module.wms.controller.admin.item.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.smart.wms.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemPageReqVO extends PageParam {

    @Schema(description = "物料编码")
    private String itemCode;

    @Schema(description = "物料名称", example = "张三")
    private String itemName;

    @Schema(description = "分类ID", example = "2492")
    private Long categoryId;

    @Schema(description = "物料类型(0-原材料 1-半成品 2-成品 3-包装材料)", example = "2")
    private Integer itemType;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "规格")
    private String spec;

    @Schema(description = "参考价格", example = "17476")
    private BigDecimal price;

    @Schema(description = "安全库存")
    private Integer safetyStock;

    @Schema(description = "状态(0-正常 1-禁用)", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
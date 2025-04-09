package cn.smart.wms.module.wms.controller.admin.inventory.vo;

import cn.smart.wms.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.smart.wms.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库存分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InventoryPageReqVO extends PageParam {

    @Schema(description = "仓库ID", example = "24551")
    private Long warehouseId;

    @Schema(description = "库位ID", example = "15018")
    private Long locationId;

    @Schema(description = "物料ID", example = "15331")
    private Long itemId;

    @Schema(description = "物料名称", example = "笔记本电脑")
    private String itemName;

    @Schema(description = "物料编码", example = "IT-001")
    private String itemCode;

    @Schema(description = "物料类型")
    private Integer itemType;

    @Schema(description = "库存数量", example = "24401")
    private Integer stockCount;

    @Schema(description = "可用数量", example = "14232")
    private Integer availableCount;

    @Schema(description = "锁定数量", example = "5102")
    private Integer lockedCount;

    @Schema(description = "状态(0-正常 1-禁用)", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
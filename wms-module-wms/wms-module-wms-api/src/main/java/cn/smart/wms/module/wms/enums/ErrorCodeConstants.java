package cn.smart.wms.module.wms.enums;

import cn.smart.wms.framework.common.exception.ErrorCode;

/**
 * WMS 错误码枚举类
 * <p>
 * WMS 系统，使用 1-002-000-000 段
 *
 * 错误码区段分配：
 * - 1002000xxx：基础数据（客户、供应商）
 * - 1002001xxx：物料与分类
 * - 1002002xxx：仓库与库位
 * - 1002003xxx：批次管理
 * - 1002004xxx：库存管理
 * - 1002005xxx：入库管理
 * - 1002006xxx：出库管理
 * - 1002007xxx：移库管理
 */
public interface ErrorCodeConstants {

    // ========== 基础数据 1002000xxx ==========
    // 客户相关错误码
    ErrorCode CUSTOMER_NOT_EXISTS = new ErrorCode(1002000001, "客户不存在");
    ErrorCode CUSTOMER_CODE_EXISTS = new ErrorCode(1002000002, "客户编码已存在");
    ErrorCode CUSTOMER_STATUS_ERROR = new ErrorCode(1002000003, "客户状态异常");

    // 供应商相关错误码
    ErrorCode SUPPLIER_NOT_EXISTS = new ErrorCode(1002000101, "供应商不存在");
    ErrorCode SUPPLIER_CODE_EXISTS = new ErrorCode(1002000102, "供应商编码已存在");
    ErrorCode SUPPLIER_STATUS_ERROR = new ErrorCode(1002000103, "供应商状态异常");

    // ========== 物料与分类 1002001xxx ==========
    // 物料相关错误码
    ErrorCode ITEM_NOT_EXISTS = new ErrorCode(1002001001, "物料不存在");
    ErrorCode ITEM_CODE_EXISTS = new ErrorCode(1002001002, "物料编码已存在");
    ErrorCode ITEM_STATUS_ERROR = new ErrorCode(1002001003, "物料状态异常");
    
    // 物料分类相关错误码
    ErrorCode ITEM_CATEGORY_NOT_EXISTS = new ErrorCode(1002001101, "物料分类不存在");
    ErrorCode ITEM_CATEGORY_CODE_EXISTS = new ErrorCode(1002001102, "物料分类编码已存在");
    ErrorCode ITEM_CATEGORY_HAS_CHILDREN = new ErrorCode(1002001103, "物料分类下存在子分类");
    ErrorCode ITEM_CATEGORY_HAS_ITEMS = new ErrorCode(1002001104, "物料分类下存在物料");

    // ========== 仓库与库位 1002002xxx ==========
    // 仓库相关错误码
    ErrorCode WAREHOUSE_NOT_EXISTS = new ErrorCode(1002002001, "仓库不存在");
    ErrorCode WAREHOUSE_CODE_EXISTS = new ErrorCode(1002002002, "仓库编码已存在");
    ErrorCode WAREHOUSE_STATUS_ERROR = new ErrorCode(1002002003, "仓库状态异常");
    ErrorCode WAREHOUSE_HAS_AREA = new ErrorCode(1002002004, "仓库下存在货区");

    // 货区相关错误码
    ErrorCode AREA_NOT_EXISTS = new ErrorCode(1002002101, "货区不存在");
    ErrorCode AREA_CODE_EXISTS = new ErrorCode(1002002102, "货区编码已存在");
    ErrorCode AREA_STATUS_ERROR = new ErrorCode(1002002103, "货区状态异常");
    ErrorCode AREA_HAS_RACK = new ErrorCode(1002002104, "货区下存在货架");

    // 货架相关错误码
    ErrorCode RACK_NOT_EXISTS = new ErrorCode(1002002201, "货架不存在");
    ErrorCode RACK_CODE_EXISTS = new ErrorCode(1002002202, "货架编码已存在");
    ErrorCode RACK_STATUS_ERROR = new ErrorCode(1002002203, "货架状态异常");
    ErrorCode RACK_HAS_LOCATION = new ErrorCode(1002002204, "货架下存在库位");

    // 库位相关错误码
    ErrorCode LOCATION_NOT_EXISTS = new ErrorCode(1002002301, "库位不存在");
    ErrorCode LOCATION_CODE_EXISTS = new ErrorCode(1002002302, "库位编码已存在");
    ErrorCode LOCATION_STATUS_ERROR = new ErrorCode(1002002303, "库位状态异常");
    ErrorCode LOCATION_OCCUPIED = new ErrorCode(1002002304, "库位已被占用");
    ErrorCode LOCATION_HAS_INVENTORY = new ErrorCode(1002002305, "库位存在库存");

    // ========== 批次管理 1002003xxx ==========
    ErrorCode BATCH_NOT_EXISTS = new ErrorCode(1002003001, "批次不存在");
    ErrorCode BATCH_CODE_EXISTS = new ErrorCode(1002003002, "批次编码已存在");
    ErrorCode BATCH_STATUS_ERROR = new ErrorCode(1002003003, "批次状态异常");
    ErrorCode BATCH_EXPIRED = new ErrorCode(1002003004, "批次已过期");
    ErrorCode BATCH_INVENTORY_NOT_EXISTS = new ErrorCode(1002003101, "批次库存不存在");
    ErrorCode BATCH_RECORD_NOT_EXISTS = new ErrorCode(1002003201, "批次记录不存在");

    // ========== 库存管理 1002004xxx ==========
    ErrorCode INVENTORY_NOT_EXISTS = new ErrorCode(1002004001, "库存不存在");
    ErrorCode INVENTORY_INSUFFICIENT = new ErrorCode(1002004002, "库存不足");
    ErrorCode INVENTORY_FROZEN = new ErrorCode(1002004003, "库存已冻结");
    ErrorCode INVENTORY_NOT_ENOUGH = new ErrorCode(1002004004, "没有足够的库存");

    ErrorCode INVENTORY_CHECK_NOT_EXISTS = new ErrorCode(1002004101, "盘点单不存在");
    ErrorCode INVENTORY_CHECK_STATUS_ERROR = new ErrorCode(1002004102, "盘点单状态异常");
    ErrorCode INVENTORY_CHECK_DETAIL_NOT_EXISTS = new ErrorCode(1002004103, "盘点单明细不存在");

    ErrorCode INVENTORY_MOVEMENT_NOT_EXISTS = new ErrorCode(1002004201, "库存移动不存在");
    ErrorCode INVENTORY_MOVEMENT_STATUS_ERROR = new ErrorCode(1002004202, "库存移动状态异常");

    // ========== 入库管理 1002005xxx ==========
    ErrorCode RECEIPT_ORDER_NOT_EXISTS = new ErrorCode(1002005001, "入库单不存在");
    ErrorCode RECEIPT_ORDER_STATUS_ERROR = new ErrorCode(1002005002, "入库单状态异常");
    ErrorCode RECEIPT_ORDER_DETAIL_NOT_EXISTS = new ErrorCode(1002005101, "入库单明细不存在");
    ErrorCode RECEIPT_RECORD_NOT_EXISTS = new ErrorCode(1002005201, "入库记录不存在");

    // ========== 出库管理 1002006xxx ==========
    ErrorCode SHIPMENT_ORDER_NOT_EXISTS = new ErrorCode(1002006001, "出库单不存在");
    ErrorCode SHIPMENT_ORDER_STATUS_ERROR = new ErrorCode(1002006002, "出库单状态异常");
    ErrorCode SHIPMENT_ORDER_DETAIL_NOT_EXISTS = new ErrorCode(1002006101, "出库单明细不存在");
    ErrorCode SHIPMENT_RECORD_NOT_EXISTS = new ErrorCode(1002006201, "出库记录不存在");
    ErrorCode SHIPMENT_ORDER_UPDATE_ONLY_DRAFT_STATUS = new ErrorCode(1002006003, "只有草稿状态的出库单可以更新");
    ErrorCode SHIPMENT_ORDER_DELETE_ONLY_DRAFT_STATUS = new ErrorCode(1002006004, "只有草稿状态的出库单可以删除");
    ErrorCode SHIPMENT_ORDER_SUBMIT_ONLY_DRAFT_STATUS = new ErrorCode(1002006005, "只有草稿状态的出库单可以提交审核");
    ErrorCode SHIPMENT_ORDER_SUBMIT_NO_DETAILS = new ErrorCode(1002006006, "出库单没有明细，不能提交审核");
    ErrorCode SHIPMENT_ORDER_APPROVE_ONLY_PENDING_STATUS = new ErrorCode(1002006007, "只有待审核状态的出库单可以审核");
    ErrorCode SHIPMENT_ORDER_CANCEL_ERROR_STATUS = new ErrorCode(1002006008, "已完成的出库单不能取消");
    ErrorCode SHIPMENT_ORDER_NOT_APPROVED = new ErrorCode(1002006009, "出库单未审核通过，不能执行出库操作");
    ErrorCode SHIPMENT_ORDER_COMPLETE_ERROR_STATUS = new ErrorCode(1002006010, "只有部分出库的出库单才能手动完成");
    ErrorCode SHIPMENT_ORDER_NOT_COMPLETE = new ErrorCode(1002006011, "出库单未完成出库操作，不能标记为完成");
    ErrorCode SHIPMENT_ORDER_QUANTITY_ERROR = new ErrorCode(1002006012, "出库数量无效或超过可出库数量");
    ErrorCode SHIPMENT_ORDER_DETAIL_MISMATCH = new ErrorCode(1002006102, "出库单明细不属于该出库单");

    // ========== 移库管理 1002007xxx ==========
    ErrorCode MOVE_ORDER_NOT_EXISTS = new ErrorCode(1002007001, "移库单不存在");
    ErrorCode MOVE_ORDER_STATUS_ERROR = new ErrorCode(1002007002, "移库单状态异常");
    ErrorCode MOVE_ORDER_DETAIL_NOT_EXISTS = new ErrorCode(1002007101, "移库单明细不存在");
    ErrorCode MOVE_RECORD_NOT_EXISTS = new ErrorCode(1002007201, "移库记录不存在");
}
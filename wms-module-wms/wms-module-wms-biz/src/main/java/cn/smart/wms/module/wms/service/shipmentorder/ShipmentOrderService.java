package cn.smart.wms.module.wms.service.shipmentorder;

import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.shipmentorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorder.ShipmentOrderDO;
import cn.smart.wms.framework.common.pojo.PageResult;

/**
 * 出库单 Service 接口
 *
 * @author 芋道源码
 */
public interface ShipmentOrderService {

    /**
     * 创建出库单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShipmentOrder(@Valid ShipmentOrderSaveReqVO createReqVO);

    /**
     * 更新出库单
     *
     * @param updateReqVO 更新信息
     */
    void updateShipmentOrder(@Valid ShipmentOrderSaveReqVO updateReqVO);

    /**
     * 删除出库单
     *
     * @param id 编号
     */
    void deleteShipmentOrder(Long id);

    /**
     * 获得出库单
     *
     * @param id 编号
     * @return 出库单
     */
    ShipmentOrderDO getShipmentOrder(Long id);

    /**
     * 获得出库单分页
     *
     * @param pageReqVO 分页查询
     * @return 出库单分页
     */
    PageResult<ShipmentOrderDO> getShipmentOrderPage(ShipmentOrderPageReqVO pageReqVO);
    
    /**
     * 创建出库单和明细（一次性保存）
     *
     * @param createReqVO 创建信息（包含出库单和明细）
     * @return 出库单ID
     */
    Long createShipmentOrderWithDetails(@Valid ShipmentOrderSaveReqVO createReqVO);
    
    /**
     * 更新出库单和明细（一次性保存）
     *
     * @param updateReqVO 更新信息（包含出库单和明细）
     */
    void updateShipmentOrderWithDetails(@Valid ShipmentOrderSaveReqVO updateReqVO);
    
    /**
     * 提交出库单审核
     *
     * @param id 出库单ID
     */
    void submitShipmentOrder(Long id);
    
    /**
     * 审核出库单
     *
     * @param id      出库单ID
     * @param approved 是否通过
     * @param remark   审核备注
     */
    void approveShipmentOrder(Long id, Boolean approved, String remark);
    
    /**
     * 取消出库单
     *
     * @param id 出库单ID
     */
    void cancelShipmentOrder(Long id);

    /**
     * 完成出库单
     *
     * @param id 出库单编号
     */
    void completeShipmentOrder(Long id);

    /**
     * 执行出库操作
     *
     * @param reqVO 出库操作请求
     */
    void executeShipment(ShipmentOperationReqVO reqVO);

}
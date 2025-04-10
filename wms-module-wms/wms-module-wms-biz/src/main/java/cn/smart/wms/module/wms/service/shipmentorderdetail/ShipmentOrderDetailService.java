package cn.smart.wms.module.wms.service.shipmentorderdetail;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailPageReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.ShipmentOrderDetailSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 出库单明细 Service 接口
 *
 * @author 芋道源码
 */
public interface ShipmentOrderDetailService {

    /**
     * 创建出库单明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShipmentOrderDetail(@Valid ShipmentOrderDetailSaveReqVO createReqVO);

    /**
     * 更新出库单明细
     *
     * @param updateReqVO 更新信息
     */
    void updateShipmentOrderDetail(@Valid ShipmentOrderDetailSaveReqVO updateReqVO);

    /**
     * 删除出库单明细
     *
     * @param id 编号
     */
    void deleteShipmentOrderDetail(Long id);

    /**
     * 获得出库单明细
     *
     * @param id 编号
     * @return 出库单明细
     */
    ShipmentOrderDetailDO getShipmentOrderDetail(Long id);

    /**
     * 获得出库单明细分页
     *
     * @param pageReqVO 分页查询
     * @return 出库单明细分页
     */
    PageResult<ShipmentOrderDetailDO> getShipmentOrderDetailPage(ShipmentOrderDetailPageReqVO pageReqVO);
    
    /**
     * 根据出库单ID获取明细列表
     *
     * @param orderId 出库单ID
     * @return 明细列表
     */
    List<ShipmentOrderDetailDO> getShipmentOrderDetailListByOrderId(Long orderId);

    /**
     * 更新出库单明细状态
     *
     * @param id     明细ID
     * @param status 新状态
     */
    void updateShipmentOrderDetailStatus(Long id, Integer status);

    /**
     * 判断出库单的所有明细是否都已完成出库
     *
     * @param orderId 出库单ID
     * @return 如果所有明细都已完成出库，则返回true；否则返回false
     */
    boolean isAllShipped(Long orderId);

    /**
     * 更新出库单明细的实际出库数量
     *
     * @param id       明细ID
     * @param quantity 出库数量增量
     */
    void updateRealCount(Long id, Integer quantity);
}
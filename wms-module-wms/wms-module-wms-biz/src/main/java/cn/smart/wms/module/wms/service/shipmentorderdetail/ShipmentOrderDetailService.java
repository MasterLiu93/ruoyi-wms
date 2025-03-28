package cn.smart.wms.module.wms.service.shipmentorderdetail;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.shipmentorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.shipmentorderdetail.ShipmentOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

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

}
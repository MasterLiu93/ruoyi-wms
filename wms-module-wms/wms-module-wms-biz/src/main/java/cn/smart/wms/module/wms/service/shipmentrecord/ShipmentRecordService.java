package cn.smart.wms.module.wms.service.shipmentrecord;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordPageReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentrecord.ShipmentRecordDO;

import javax.validation.Valid;

/**
 * 出库操作记录 Service 接口
 *
 * @author 芋道源码
 */
public interface ShipmentRecordService {

    /**
     * 创建出库操作记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createShipmentRecord(@Valid ShipmentRecordSaveReqVO createReqVO);

    /**
     * 更新出库操作记录
     *
     * @param updateReqVO 更新信息
     */
    void updateShipmentRecord(@Valid ShipmentRecordSaveReqVO updateReqVO);

    /**
     * 删除出库操作记录
     *
     * @param id 编号
     */
    void deleteShipmentRecord(Long id);

    /**
     * 获得出库操作记录
     *
     * @param id 编号
     * @return 出库操作记录
     */
    ShipmentRecordDO getShipmentRecord(Long id);

    /**
     * 获得出库操作记录分页
     *
     * @param pageReqVO 分页查询
     * @return 出库操作记录分页
     */
    PageResult<ShipmentRecordDO> getShipmentRecordPage(ShipmentRecordPageReqVO pageReqVO);

}
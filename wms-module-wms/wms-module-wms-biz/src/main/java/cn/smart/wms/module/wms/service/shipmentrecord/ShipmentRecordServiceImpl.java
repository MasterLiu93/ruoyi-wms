package cn.smart.wms.module.wms.service.shipmentrecord;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordPageReqVO;
import cn.smart.wms.module.wms.controller.admin.shipmentrecord.vo.ShipmentRecordSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.shipmentrecord.ShipmentRecordDO;
import cn.smart.wms.module.wms.dal.mysql.shipmentrecord.ShipmentRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.SHIPMENT_RECORD_NOT_EXISTS;

/**
 * 出库操作记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ShipmentRecordServiceImpl implements ShipmentRecordService {

    @Resource
    private ShipmentRecordMapper shipmentRecordMapper;

    @Override
    public Long createShipmentRecord(ShipmentRecordSaveReqVO createReqVO) {
        // 插入
        ShipmentRecordDO shipmentRecord = BeanUtils.toBean(createReqVO, ShipmentRecordDO.class);
        shipmentRecordMapper.insert(shipmentRecord);
        // 返回
        return shipmentRecord.getId();
    }

    @Override
    public void updateShipmentRecord(ShipmentRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateShipmentRecordExists(updateReqVO.getId());
        // 更新
        ShipmentRecordDO updateObj = BeanUtils.toBean(updateReqVO, ShipmentRecordDO.class);
        shipmentRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteShipmentRecord(Long id) {
        // 校验存在
        validateShipmentRecordExists(id);
        // 删除
        shipmentRecordMapper.deleteById(id);
    }

    private void validateShipmentRecordExists(Long id) {
        if (shipmentRecordMapper.selectById(id) == null) {
            throw exception(SHIPMENT_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public ShipmentRecordDO getShipmentRecord(Long id) {
        return shipmentRecordMapper.selectById(id);
    }

    @Override
    public PageResult<ShipmentRecordDO> getShipmentRecordPage(ShipmentRecordPageReqVO pageReqVO) {
        return shipmentRecordMapper.selectPage(pageReqVO);
    }

}
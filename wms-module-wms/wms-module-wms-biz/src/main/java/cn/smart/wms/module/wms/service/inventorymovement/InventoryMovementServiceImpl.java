package cn.smart.wms.module.wms.service.inventorymovement;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.smart.wms.module.wms.controller.admin.inventorymovement.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorymovement.InventoryMovementDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.inventorymovement.InventoryMovementMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存移动记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InventoryMovementServiceImpl implements InventoryMovementService {

    @Resource
    private InventoryMovementMapper inventoryMovementMapper;

    @Override
    public Long createInventoryMovement(InventoryMovementSaveReqVO createReqVO) {
        // 插入
        InventoryMovementDO inventoryMovement = BeanUtils.toBean(createReqVO, InventoryMovementDO.class);
        inventoryMovementMapper.insert(inventoryMovement);
        // 返回
        return inventoryMovement.getId();
    }

    @Override
    public void updateInventoryMovement(InventoryMovementSaveReqVO updateReqVO) {
        // 校验存在
        validateInventoryMovementExists(updateReqVO.getId());
        // 更新
        InventoryMovementDO updateObj = BeanUtils.toBean(updateReqVO, InventoryMovementDO.class);
        inventoryMovementMapper.updateById(updateObj);
    }

    @Override
    public void deleteInventoryMovement(Long id) {
        // 校验存在
        validateInventoryMovementExists(id);
        // 删除
        inventoryMovementMapper.deleteById(id);
    }

    private void validateInventoryMovementExists(Long id) {
        if (inventoryMovementMapper.selectById(id) == null) {
            throw exception(INVENTORY_MOVEMENT_NOT_EXISTS);
        }
    }

    @Override
    public InventoryMovementDO getInventoryMovement(Long id) {
        return inventoryMovementMapper.selectById(id);
    }

    @Override
    public PageResult<InventoryMovementDO> getInventoryMovementPage(InventoryMovementPageReqVO pageReqVO) {
        return inventoryMovementMapper.selectPage(pageReqVO);
    }

}
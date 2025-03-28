package cn.smart.wms.module.wms.service.inventory;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.inventory.InventoryMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InventoryServiceImpl implements InventoryService {

    @Resource
    private InventoryMapper inventoryMapper;

    @Override
    public Long createInventory(InventorySaveReqVO createReqVO) {
        // 插入
        InventoryDO inventory = BeanUtils.toBean(createReqVO, InventoryDO.class);
        inventoryMapper.insert(inventory);
        // 返回
        return inventory.getId();
    }

    @Override
    public void updateInventory(InventorySaveReqVO updateReqVO) {
        // 校验存在
        validateInventoryExists(updateReqVO.getId());
        // 更新
        InventoryDO updateObj = BeanUtils.toBean(updateReqVO, InventoryDO.class);
        inventoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteInventory(Long id) {
        // 校验存在
        validateInventoryExists(id);
        // 删除
        inventoryMapper.deleteById(id);
    }

    private void validateInventoryExists(Long id) {
        if (inventoryMapper.selectById(id) == null) {
            throw exception(INVENTORY_NOT_EXISTS);
        }
    }

    @Override
    public InventoryDO getInventory(Long id) {
        return inventoryMapper.selectById(id);
    }

    @Override
    public PageResult<InventoryDO> getInventoryPage(InventoryPageReqVO pageReqVO) {
        return inventoryMapper.selectPage(pageReqVO);
    }

}
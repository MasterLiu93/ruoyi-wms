package cn.smart.wms.module.wms.service.inventorycheck;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.inventorycheck.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheck.InventoryCheckDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.inventorycheck.InventoryCheckMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存盘点单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InventoryCheckServiceImpl implements InventoryCheckService {

    @Resource
    private InventoryCheckMapper inventoryCheckMapper;

    @Override
    public Long createInventoryCheck(InventoryCheckSaveReqVO createReqVO) {
        // 插入
        InventoryCheckDO inventoryCheck = BeanUtils.toBean(createReqVO, InventoryCheckDO.class);
        inventoryCheckMapper.insert(inventoryCheck);
        // 返回
        return inventoryCheck.getId();
    }

    @Override
    public void updateInventoryCheck(InventoryCheckSaveReqVO updateReqVO) {
        // 校验存在
        validateInventoryCheckExists(updateReqVO.getId());
        // 更新
        InventoryCheckDO updateObj = BeanUtils.toBean(updateReqVO, InventoryCheckDO.class);
        inventoryCheckMapper.updateById(updateObj);
    }

    @Override
    public void deleteInventoryCheck(Long id) {
        // 校验存在
        validateInventoryCheckExists(id);
        // 删除
        inventoryCheckMapper.deleteById(id);
    }

    private void validateInventoryCheckExists(Long id) {
        if (inventoryCheckMapper.selectById(id) == null) {
            throw exception(INVENTORY_CHECK_NOT_EXISTS);
        }
    }

    @Override
    public InventoryCheckDO getInventoryCheck(Long id) {
        return inventoryCheckMapper.selectById(id);
    }

    @Override
    public PageResult<InventoryCheckDO> getInventoryCheckPage(InventoryCheckPageReqVO pageReqVO) {
        return inventoryCheckMapper.selectPage(pageReqVO);
    }

}
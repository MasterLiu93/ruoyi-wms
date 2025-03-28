package cn.smart.wms.module.wms.service.item;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.item.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.item.ItemDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.item.ItemMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 物料 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Override
    public Long createItem(ItemSaveReqVO createReqVO) {
        // 插入
        ItemDO item = BeanUtils.toBean(createReqVO, ItemDO.class);
        itemMapper.insert(item);
        // 返回
        return item.getId();
    }

    @Override
    public void updateItem(ItemSaveReqVO updateReqVO) {
        // 校验存在
        validateItemExists(updateReqVO.getId());
        // 更新
        ItemDO updateObj = BeanUtils.toBean(updateReqVO, ItemDO.class);
        itemMapper.updateById(updateObj);
    }

    @Override
    public void deleteItem(Long id) {
        // 校验存在
        validateItemExists(id);
        // 删除
        itemMapper.deleteById(id);
    }

    private void validateItemExists(Long id) {
        if (itemMapper.selectById(id) == null) {
            throw exception(ITEM_NOT_EXISTS);
        }
    }

    @Override
    public ItemDO getItem(Long id) {
        return itemMapper.selectById(id);
    }

    @Override
    public PageResult<ItemDO> getItemPage(ItemPageReqVO pageReqVO) {
        return itemMapper.selectPage(pageReqVO);
    }

}
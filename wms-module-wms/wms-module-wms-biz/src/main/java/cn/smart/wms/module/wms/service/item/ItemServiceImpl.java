package cn.smart.wms.module.wms.service.item;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.item.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.item.ItemDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.item.ItemMapper;
import cn.smart.wms.module.wms.service.itemcategory.ItemCategoryService;
import cn.smart.wms.module.wms.dal.dataobject.itemcategory.ItemCategoryDO;

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
    
    @Resource
    private ItemCategoryService itemCategoryService;

    @Override
    public Long createItem(ItemSaveReqVO createReqVO) {
        // 校验物料编码是否唯一
        validateItemCodeUnique(null, createReqVO.getItemCode());
        // 校验物料分类是否存在
        validateItemCategoryExists(createReqVO.getCategoryId());
        
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
        // 校验物料编码是否唯一
        validateItemCodeUnique(updateReqVO.getId(), updateReqVO.getItemCode());
        // 校验物料分类是否存在
        validateItemCategoryExists(updateReqVO.getCategoryId());
        
        // 更新
        ItemDO updateObj = BeanUtils.toBean(updateReqVO, ItemDO.class);
        itemMapper.updateById(updateObj);
    }

    @Override
    public void deleteItem(Long id) {
        // 校验存在
        validateItemExists(id);
        // TODO 校验是否有库存，有则不允许删除
        // TODO 校验是否有入库单等关联，有则不允许删除
        
        // 删除
        itemMapper.deleteById(id);
    }

    private void validateItemExists(Long id) {
        if (itemMapper.selectById(id) == null) {
            throw exception(ITEM_NOT_EXISTS);
        }
    }
    
    /**
     * 校验物料编码是否唯一
     *
     * @param id 物料ID
     * @param itemCode 物料编码
     */
    private void validateItemCodeUnique(Long id, String itemCode) {
        // 如果编码为空或为空字符串，直接返回，不进行唯一性校验
        if (itemCode == null || itemCode.isEmpty()) {
            return;
        }
        
        ItemDO item = itemMapper.selectByItemCode(itemCode);
        if (item == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的物料
        if (id == null) {
            throw exception(ITEM_CODE_EXISTS);
        }
        if (!item.getId().equals(id)) {
            throw exception(ITEM_CODE_EXISTS);
        }
    }
    
    /**
     * 校验物料分类是否存在
     *
     * @param categoryId 分类ID
     */
    private void validateItemCategoryExists(Long categoryId) {
        ItemCategoryDO category = itemCategoryService.getItemCategory(categoryId);
        if (category == null) {
            throw exception(ITEM_CATEGORY_NOT_EXISTS);
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

    @Override
    public ItemDO getItemByCode(String itemCode) {
        return itemMapper.selectByItemCode(itemCode);
    }
    
    @Override
    public ItemDO getItemByName(String itemName) {
        return itemMapper.selectByItemName(itemName);
    }
    
    @Override
    public Long getIdByCodeOrName(String itemCode, String itemName) {
        // 优先通过编码查询
        if (itemCode != null && !itemCode.isEmpty()) {
            ItemDO item = getItemByCode(itemCode);
            if (item != null) {
                return item.getId();
            }
        }
        
        // 其次通过名称查询
        if (itemName != null && !itemName.isEmpty()) {
            ItemDO item = getItemByName(itemName);
            if (item != null) {
                return item.getId();
            }
        }
        
        return null;
    }
    
    @Override
    public List<ItemDO> getItemList(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return itemMapper.selectBatchIds(ids);
    }
}
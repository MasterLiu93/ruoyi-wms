package cn.smart.wms.module.wms.service.itemcategory;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.itemcategory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.itemcategory.ItemCategoryDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.itemcategory.ItemCategoryMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 物料分类 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Resource
    private ItemCategoryMapper itemCategoryMapper;

    @Override
    public Long createItemCategory(ItemCategorySaveReqVO createReqVO) {
        // 插入
        ItemCategoryDO itemCategory = BeanUtils.toBean(createReqVO, ItemCategoryDO.class);
        itemCategoryMapper.insert(itemCategory);
        // 返回
        return itemCategory.getId();
    }

    @Override
    public void updateItemCategory(ItemCategorySaveReqVO updateReqVO) {
        // 校验存在
        validateItemCategoryExists(updateReqVO.getId());
        // 更新
        ItemCategoryDO updateObj = BeanUtils.toBean(updateReqVO, ItemCategoryDO.class);
        itemCategoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteItemCategory(Long id) {
        // 校验存在
        validateItemCategoryExists(id);
        // 删除
        itemCategoryMapper.deleteById(id);
    }

    private void validateItemCategoryExists(Long id) {
        if (itemCategoryMapper.selectById(id) == null) {
            throw exception(ITEM_CATEGORY_NOT_EXISTS);
        }
    }

    @Override
    public ItemCategoryDO getItemCategory(Long id) {
        return itemCategoryMapper.selectById(id);
    }

    @Override
    public PageResult<ItemCategoryDO> getItemCategoryPage(ItemCategoryPageReqVO pageReqVO) {
        return itemCategoryMapper.selectPage(pageReqVO);
    }

}
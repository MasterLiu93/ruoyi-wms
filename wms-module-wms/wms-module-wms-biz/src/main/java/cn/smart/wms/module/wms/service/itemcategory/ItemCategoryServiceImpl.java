package cn.smart.wms.module.wms.service.itemcategory;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import cn.smart.wms.module.wms.controller.admin.itemcategory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.itemcategory.ItemCategoryDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;

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
    
    @Override
    public List<ItemCategoryTreeRespVO> getItemCategoryTree(String categoryName, String categoryCode, Integer status) {
        // 1. 查询所有物料分类
        List<ItemCategoryDO> list = itemCategoryMapper.selectList(
            new LambdaQueryWrapperX<ItemCategoryDO>()
                .likeIfPresent(ItemCategoryDO::getCategoryName, categoryName)
                .eqIfPresent(ItemCategoryDO::getCategoryCode, categoryCode)
                .eqIfPresent(ItemCategoryDO::getStatus, status)
                .orderByAsc(ItemCategoryDO::getSort));
        
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 2. 转换为 VO 对象
        List<ItemCategoryTreeRespVO> itemCategoryVOList = BeanUtils.toBean(list, ItemCategoryTreeRespVO.class);
        
        // 3. 构建树形结构
        Map<Long, List<ItemCategoryTreeRespVO>> parentIdChildrenMap = itemCategoryVOList.stream()
                .collect(Collectors.groupingBy(ItemCategoryTreeRespVO::getParentId));
        
        // 4. 设置子节点，并返回根节点
        for (ItemCategoryTreeRespVO categoryVO : itemCategoryVOList) {
            categoryVO.setChildren(parentIdChildrenMap.getOrDefault(categoryVO.getId(), Collections.emptyList()));
        }
        
        // 5. 返回根节点(即parentId为0或null的节点)
        return itemCategoryVOList.stream()
                .filter(categoryVO -> categoryVO.getParentId() == null || categoryVO.getParentId() == 0)
                .collect(Collectors.toList());
    }
}
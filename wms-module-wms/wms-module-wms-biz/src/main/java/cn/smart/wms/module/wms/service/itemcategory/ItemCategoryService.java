package cn.smart.wms.module.wms.service.itemcategory;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.itemcategory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.itemcategory.ItemCategoryDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 物料分类 Service 接口
 *
 * @author 芋道源码
 */
public interface ItemCategoryService {

    /**
     * 创建物料分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createItemCategory(@Valid ItemCategorySaveReqVO createReqVO);

    /**
     * 更新物料分类
     *
     * @param updateReqVO 更新信息
     */
    void updateItemCategory(@Valid ItemCategorySaveReqVO updateReqVO);

    /**
     * 删除物料分类
     *
     * @param id 编号
     */
    void deleteItemCategory(Long id);

    /**
     * 获得物料分类
     *
     * @param id 编号
     * @return 物料分类
     */
    ItemCategoryDO getItemCategory(Long id);

    /**
     * 获得物料分类分页
     *
     * @param pageReqVO 分页查询
     * @return 物料分类分页
     */
    PageResult<ItemCategoryDO> getItemCategoryPage(ItemCategoryPageReqVO pageReqVO);

}
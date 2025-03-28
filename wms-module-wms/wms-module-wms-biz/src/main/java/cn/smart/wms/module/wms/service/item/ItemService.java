package cn.smart.wms.module.wms.service.item;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.item.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.item.ItemDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 物料 Service 接口
 *
 * @author 芋道源码
 */
public interface ItemService {

    /**
     * 创建物料
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createItem(@Valid ItemSaveReqVO createReqVO);

    /**
     * 更新物料
     *
     * @param updateReqVO 更新信息
     */
    void updateItem(@Valid ItemSaveReqVO updateReqVO);

    /**
     * 删除物料
     *
     * @param id 编号
     */
    void deleteItem(Long id);

    /**
     * 获得物料
     *
     * @param id 编号
     * @return 物料
     */
    ItemDO getItem(Long id);

    /**
     * 获得物料分页
     *
     * @param pageReqVO 分页查询
     * @return 物料分页
     */
    PageResult<ItemDO> getItemPage(ItemPageReqVO pageReqVO);

}
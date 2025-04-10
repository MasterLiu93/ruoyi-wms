package cn.smart.wms.module.wms.service.item;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.item.vo.ItemPageReqVO;
import cn.smart.wms.module.wms.controller.admin.item.vo.ItemSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.item.ItemDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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

    /**
     * 通过物料编码获取物料
     *
     * @param itemCode 物料编码
     * @return 物料对象
     */
    ItemDO getItemByCode(String itemCode);
    
    /**
     * 通过物料名称获取物料
     *
     * @param itemName 物料名称
     * @return 物料对象
     */
    ItemDO getItemByName(String itemName);
    
    /**
     * 通过物料编码或名称查询物料ID
     *
     * @param itemCode 物料编码
     * @param itemName 物料名称
     * @return 物料ID
     */
    Long getIdByCodeOrName(String itemCode, String itemName);
    
    /**
     * 批量获取物料信息
     *
     * @param ids 物料ID集合
     * @return 物料列表
     */
    List<ItemDO> getItemList(Set<Long> ids);

}
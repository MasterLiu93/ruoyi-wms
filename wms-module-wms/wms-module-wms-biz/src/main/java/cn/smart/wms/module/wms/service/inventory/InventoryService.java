package cn.smart.wms.module.wms.service.inventory;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.inventory.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventory.InventoryDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 库存 Service 接口
 *
 * @author 芋道源码
 */
public interface InventoryService {

    /**
     * 创建库存
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInventory(@Valid InventorySaveReqVO createReqVO);

    /**
     * 更新库存
     *
     * @param updateReqVO 更新信息
     */
    void updateInventory(@Valid InventorySaveReqVO updateReqVO);

    /**
     * 删除库存
     *
     * @param id 编号
     */
    void deleteInventory(Long id);

    /**
     * 获得库存
     *
     * @param id 编号
     * @return 库存
     */
    InventoryDO getInventory(Long id);

    /**
     * 获得库存分页
     *
     * @param pageReqVO 分页查询
     * @return 库存分页
     */
    PageResult<InventoryDO> getInventoryPage(InventoryPageReqVO pageReqVO);

}
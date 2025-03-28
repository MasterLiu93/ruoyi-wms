package cn.smart.wms.module.wms.service.inventorycheck;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.inventorycheck.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheck.InventoryCheckDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 库存盘点单 Service 接口
 *
 * @author 芋道源码
 */
public interface InventoryCheckService {

    /**
     * 创建库存盘点单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInventoryCheck(@Valid InventoryCheckSaveReqVO createReqVO);

    /**
     * 更新库存盘点单
     *
     * @param updateReqVO 更新信息
     */
    void updateInventoryCheck(@Valid InventoryCheckSaveReqVO updateReqVO);

    /**
     * 删除库存盘点单
     *
     * @param id 编号
     */
    void deleteInventoryCheck(Long id);

    /**
     * 获得库存盘点单
     *
     * @param id 编号
     * @return 库存盘点单
     */
    InventoryCheckDO getInventoryCheck(Long id);

    /**
     * 获得库存盘点单分页
     *
     * @param pageReqVO 分页查询
     * @return 库存盘点单分页
     */
    PageResult<InventoryCheckDO> getInventoryCheckPage(InventoryCheckPageReqVO pageReqVO);

}
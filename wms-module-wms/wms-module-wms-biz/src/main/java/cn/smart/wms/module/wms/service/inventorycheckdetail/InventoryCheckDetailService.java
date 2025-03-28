package cn.smart.wms.module.wms.service.inventorycheckdetail;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheckdetail.InventoryCheckDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 库存盘点明细 Service 接口
 *
 * @author 芋道源码
 */
public interface InventoryCheckDetailService {

    /**
     * 创建库存盘点明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInventoryCheckDetail(@Valid InventoryCheckDetailSaveReqVO createReqVO);

    /**
     * 更新库存盘点明细
     *
     * @param updateReqVO 更新信息
     */
    void updateInventoryCheckDetail(@Valid InventoryCheckDetailSaveReqVO updateReqVO);

    /**
     * 删除库存盘点明细
     *
     * @param id 编号
     */
    void deleteInventoryCheckDetail(Long id);

    /**
     * 获得库存盘点明细
     *
     * @param id 编号
     * @return 库存盘点明细
     */
    InventoryCheckDetailDO getInventoryCheckDetail(Long id);

    /**
     * 获得库存盘点明细分页
     *
     * @param pageReqVO 分页查询
     * @return 库存盘点明细分页
     */
    PageResult<InventoryCheckDetailDO> getInventoryCheckDetailPage(InventoryCheckDetailPageReqVO pageReqVO);

}
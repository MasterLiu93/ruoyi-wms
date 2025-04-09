package cn.smart.wms.module.wms.service.inventorymovement;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.wms.controller.admin.inventorymovement.vo.InventoryMovementPageReqVO;
import cn.smart.wms.module.wms.controller.admin.inventorymovement.vo.InventoryMovementSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventorymovement.InventoryMovementDO;

import javax.validation.Valid;

/**
 * 库存移动记录 Service 接口
 *
 * @author 芋道源码
 */
public interface InventoryMovementService {

    /**
     * 创建库存移动记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInventoryMovement(@Valid InventoryMovementSaveReqVO createReqVO);

    /**
     * 更新库存移动记录
     *
     * @param updateReqVO 更新信息
     */
    void updateInventoryMovement(@Valid InventoryMovementSaveReqVO updateReqVO);

    /**
     * 删除库存移动记录
     *
     * @param id 编号
     */
    void deleteInventoryMovement(Long id);

    /**
     * 获得库存移动记录
     *
     * @param id 编号
     * @return 库存移动记录
     */
    InventoryMovementDO getInventoryMovement(Long id);

    /**
     * 获得库存移动记录分页
     *
     * @param pageReqVO 分页查询
     * @return 库存移动记录分页
     */
    PageResult<InventoryMovementDO> getInventoryMovementPage(InventoryMovementPageReqVO pageReqVO);

}
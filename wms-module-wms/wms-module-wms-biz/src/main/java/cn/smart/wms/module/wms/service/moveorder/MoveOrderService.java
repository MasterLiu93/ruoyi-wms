package cn.smart.wms.module.wms.service.moveorder;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.moveorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moveorder.MoveOrderDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 移库单 Service 接口
 *
 * @author 芋道源码
 */
public interface MoveOrderService {

    /**
     * 创建移库单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMoveOrder(@Valid MoveOrderSaveReqVO createReqVO);

    /**
     * 更新移库单
     *
     * @param updateReqVO 更新信息
     */
    void updateMoveOrder(@Valid MoveOrderSaveReqVO updateReqVO);

    /**
     * 删除移库单
     *
     * @param id 编号
     */
    void deleteMoveOrder(Long id);

    /**
     * 获得移库单
     *
     * @param id 编号
     * @return 移库单
     */
    MoveOrderDO getMoveOrder(Long id);

    /**
     * 获得移库单分页
     *
     * @param pageReqVO 分页查询
     * @return 移库单分页
     */
    PageResult<MoveOrderDO> getMoveOrderPage(MoveOrderPageReqVO pageReqVO);

}
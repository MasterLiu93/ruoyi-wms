package cn.smart.wms.module.wms.service.moveorder;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.moveorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moveorder.MoveOrderDO;
import cn.smart.wms.framework.common.pojo.PageResult;

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
    
    /**
     * 获得移库单列表
     *
     * @param pageReqVO 查询条件
     * @return 移库单列表
     */
    List<MoveOrderDO> getMoveOrderList(MoveOrderPageReqVO pageReqVO);

    /**
     * 审核移库单
     *
     * @param id 移库单ID
     * @param approved 是否审核通过
     * @param remark 审核备注
     */
    void approveMoveOrder(Long id, Boolean approved, String remark);
    
    /**
     * 执行移库操作 - 针对单个明细
     *
     * @param detailId 移库单明细ID
     * @param count 移库数量
     * @param remark 备注
     * @return 移库记录ID
     */
    Long executeMoveByDetail(Long detailId, Integer count, String remark);
    
    /**
     * 执行移库操作 - 针对整个移库单
     *
     * @param id 移库单ID
     * @param moveDetails 移库明细列表，包含数量等信息
     * @return 移库记录ID列表
     */
    List<Long> executeMove(Long id, List<MoveOperationReqVO> moveDetails);
    
    /**
     * 取消移库单
     *
     * @param id 移库单ID
     * @param remark 取消原因
     */
    void cancelMoveOrder(Long id, String remark);
    
    /**
     * 完成移库单
     *
     * @param id 移库单ID
     */
    void completeMoveOrder(Long id);

}
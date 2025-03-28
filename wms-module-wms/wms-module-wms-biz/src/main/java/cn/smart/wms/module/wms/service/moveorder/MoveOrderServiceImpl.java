package cn.smart.wms.module.wms.service.moveorder;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.moveorder.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moveorder.MoveOrderDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.moveorder.MoveOrderMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 移库单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MoveOrderServiceImpl implements MoveOrderService {

    @Resource
    private MoveOrderMapper moveOrderMapper;

    @Override
    public Long createMoveOrder(MoveOrderSaveReqVO createReqVO) {
        // 插入
        MoveOrderDO moveOrder = BeanUtils.toBean(createReqVO, MoveOrderDO.class);
        moveOrderMapper.insert(moveOrder);
        // 返回
        return moveOrder.getId();
    }

    @Override
    public void updateMoveOrder(MoveOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateMoveOrderExists(updateReqVO.getId());
        // 更新
        MoveOrderDO updateObj = BeanUtils.toBean(updateReqVO, MoveOrderDO.class);
        moveOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteMoveOrder(Long id) {
        // 校验存在
        validateMoveOrderExists(id);
        // 删除
        moveOrderMapper.deleteById(id);
    }

    private void validateMoveOrderExists(Long id) {
        if (moveOrderMapper.selectById(id) == null) {
            throw exception(MOVE_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public MoveOrderDO getMoveOrder(Long id) {
        return moveOrderMapper.selectById(id);
    }

    @Override
    public PageResult<MoveOrderDO> getMoveOrderPage(MoveOrderPageReqVO pageReqVO) {
        return moveOrderMapper.selectPage(pageReqVO);
    }

}
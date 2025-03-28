package cn.smart.wms.module.wms.service.moveorderdetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moveorderdetail.MoveOrderDetailDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.moveorderdetail.MoveOrderDetailMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 移库单明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MoveOrderDetailServiceImpl implements MoveOrderDetailService {

    @Resource
    private MoveOrderDetailMapper moveOrderDetailMapper;

    @Override
    public Long createMoveOrderDetail(MoveOrderDetailSaveReqVO createReqVO) {
        // 插入
        MoveOrderDetailDO moveOrderDetail = BeanUtils.toBean(createReqVO, MoveOrderDetailDO.class);
        moveOrderDetailMapper.insert(moveOrderDetail);
        // 返回
        return moveOrderDetail.getId();
    }

    @Override
    public void updateMoveOrderDetail(MoveOrderDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateMoveOrderDetailExists(updateReqVO.getId());
        // 更新
        MoveOrderDetailDO updateObj = BeanUtils.toBean(updateReqVO, MoveOrderDetailDO.class);
        moveOrderDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteMoveOrderDetail(Long id) {
        // 校验存在
        validateMoveOrderDetailExists(id);
        // 删除
        moveOrderDetailMapper.deleteById(id);
    }

    private void validateMoveOrderDetailExists(Long id) {
        if (moveOrderDetailMapper.selectById(id) == null) {
            throw exception(MOVE_ORDER_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public MoveOrderDetailDO getMoveOrderDetail(Long id) {
        return moveOrderDetailMapper.selectById(id);
    }

    @Override
    public PageResult<MoveOrderDetailDO> getMoveOrderDetailPage(MoveOrderDetailPageReqVO pageReqVO) {
        return moveOrderDetailMapper.selectPage(pageReqVO);
    }

}
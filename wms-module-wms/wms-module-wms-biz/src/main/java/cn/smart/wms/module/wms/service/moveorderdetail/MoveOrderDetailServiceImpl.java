package cn.smart.wms.module.wms.service.moveorderdetail;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo.MoveOrderDetailPageReqVO;
import cn.smart.wms.module.wms.controller.admin.moveorderdetail.vo.MoveOrderDetailSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.moveorderdetail.MoveOrderDetailDO;
import cn.smart.wms.module.wms.dal.mysql.moveorderdetail.MoveOrderDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.MOVE_ORDER_DETAIL_NOT_EXISTS;

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
        // 设置默认值
        if (moveOrderDetail.getRealCount() == null) {
            moveOrderDetail.setRealCount(0); // 默认实际移库数量为0
        }
        if (moveOrderDetail.getStatus() == null) {
            moveOrderDetail.setStatus(0); // 默认状态为未移库
        }
        moveOrderDetailMapper.insert(moveOrderDetail);
        // 返回
        return moveOrderDetail.getId();
    }

    @Override
    public void updateMoveOrderDetail(MoveOrderDetailSaveReqVO updateReqVO) {
        // 校验存在
        this.validateMoveOrderDetailExists(updateReqVO.getId());
        // 更新
        MoveOrderDetailDO updateObj = BeanUtils.toBean(updateReqVO, MoveOrderDetailDO.class);
        moveOrderDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteMoveOrderDetail(Long id) {
        // 校验存在
        this.validateMoveOrderDetailExists(id);
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
    
    @Override
    public List<MoveOrderDetailDO> getMoveOrderDetailListByMoveOrderId(Long moveOrderId) {
        return moveOrderDetailMapper.selectList(MoveOrderDetailDO::getMoveOrderId, moveOrderId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> batchCreateMoveOrderDetail(List<MoveOrderDetailSaveReqVO> details) {
        List<Long> ids = new ArrayList<>();
        for (MoveOrderDetailSaveReqVO detail : details) {
            Long id = createMoveOrderDetail(detail);
            ids.add(id);
        }
        return ids;
    }
    
    @Override
    public void updateMoveOrderDetailStatus(Long id, Integer realCount, Integer status) {
        // 校验存在
        this.validateMoveOrderDetailExists(id);
        
        // 更新对象
        MoveOrderDetailDO updateObj = new MoveOrderDetailDO();
        updateObj.setId(id);
        updateObj.setRealCount(realCount);
        updateObj.setStatus(status);
        
        moveOrderDetailMapper.updateById(updateObj);
    }
}
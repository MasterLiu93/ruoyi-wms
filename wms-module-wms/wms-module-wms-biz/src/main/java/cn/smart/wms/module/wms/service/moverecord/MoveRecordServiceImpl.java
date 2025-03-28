package cn.smart.wms.module.wms.service.moverecord;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.moverecord.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moverecord.MoveRecordDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.moverecord.MoveRecordMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 移库操作记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class MoveRecordServiceImpl implements MoveRecordService {

    @Resource
    private MoveRecordMapper moveRecordMapper;

    @Override
    public Long createMoveRecord(MoveRecordSaveReqVO createReqVO) {
        // 插入
        MoveRecordDO moveRecord = BeanUtils.toBean(createReqVO, MoveRecordDO.class);
        moveRecordMapper.insert(moveRecord);
        // 返回
        return moveRecord.getId();
    }

    @Override
    public void updateMoveRecord(MoveRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateMoveRecordExists(updateReqVO.getId());
        // 更新
        MoveRecordDO updateObj = BeanUtils.toBean(updateReqVO, MoveRecordDO.class);
        moveRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteMoveRecord(Long id) {
        // 校验存在
        validateMoveRecordExists(id);
        // 删除
        moveRecordMapper.deleteById(id);
    }

    private void validateMoveRecordExists(Long id) {
        if (moveRecordMapper.selectById(id) == null) {
            throw exception(MOVE_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public MoveRecordDO getMoveRecord(Long id) {
        return moveRecordMapper.selectById(id);
    }

    @Override
    public PageResult<MoveRecordDO> getMoveRecordPage(MoveRecordPageReqVO pageReqVO) {
        return moveRecordMapper.selectPage(pageReqVO);
    }

}
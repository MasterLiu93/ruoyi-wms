package cn.smart.wms.module.wms.service.rack;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.rack.vo.RackPageReqVO;
import cn.smart.wms.module.wms.controller.admin.rack.vo.RackSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.rack.RackDO;
import cn.smart.wms.module.wms.dal.mysql.rack.RackMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.RACK_NOT_EXISTS;

/**
 * 货架 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class RackServiceImpl implements RackService {

    @Resource
    private RackMapper rackMapper;

    @Override
    public Long createRack(RackSaveReqVO createReqVO) {
        // 插入
        RackDO rack = BeanUtils.toBean(createReqVO, RackDO.class);
        rackMapper.insert(rack);
        // 返回
        return rack.getId();
    }

    @Override
    public void updateRack(RackSaveReqVO updateReqVO) {
        // 校验存在
        validateRackExists(updateReqVO.getId());
        // 更新
        RackDO updateObj = BeanUtils.toBean(updateReqVO, RackDO.class);
        rackMapper.updateById(updateObj);
    }

    @Override
    public void deleteRack(Long id) {
        // 校验存在
        validateRackExists(id);
        // 删除
        rackMapper.deleteById(id);
    }

    private void validateRackExists(Long id) {
        if (rackMapper.selectById(id) == null) {
            throw exception(RACK_NOT_EXISTS);
        }
    }

    @Override
    public RackDO getRack(Long id) {
        return rackMapper.selectById(id);
    }

    @Override
    public PageResult<RackDO> getRackPage(RackPageReqVO pageReqVO) {
        return rackMapper.selectPage(pageReqVO);
    }

    @Override
    public List<RackDO> getRackListByAreaId(Long areaId) {
        if (areaId == null) {
            return Collections.emptyList();
        }
        return rackMapper.selectListByAreaId(areaId);
    }

    @Override
    public List<RackDO> getRackList(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return rackMapper.selectBatchIds(ids);
    }

}
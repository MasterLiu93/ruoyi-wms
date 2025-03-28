package cn.smart.wms.module.wms.service.area;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.smart.wms.module.wms.controller.admin.area.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.area.AreaDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.area.WmsAreaMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 货区 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class WmsAreaServiceImpl implements WmsAreaService {

    @Resource
    private WmsAreaMapper areaMapper;

    @Override
    public Long createArea(AreaSaveReqVO createReqVO) {
        // 插入
        AreaDO area = BeanUtils.toBean(createReqVO, AreaDO.class);
        areaMapper.insert(area);
        // 返回
        return area.getId();
    }

    @Override
    public void updateArea(AreaSaveReqVO updateReqVO) {
        // 校验存在
        validateAreaExists(updateReqVO.getId());
        // 更新
        AreaDO updateObj = BeanUtils.toBean(updateReqVO, AreaDO.class);
        areaMapper.updateById(updateObj);
    }

    @Override
    public void deleteArea(Long id) {
        // 校验存在
        validateAreaExists(id);
        // 删除
        areaMapper.deleteById(id);
    }

    private void validateAreaExists(Long id) {
        if (areaMapper.selectById(id) == null) {
            throw exception(AREA_NOT_EXISTS);
        }
    }

    @Override
    public AreaDO getArea(Long id) {
        return areaMapper.selectById(id);
    }

    @Override
    public PageResult<AreaDO> getAreaPage(AreaPageReqVO pageReqVO) {
        return areaMapper.selectPage(pageReqVO);
    }

}
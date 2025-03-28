package cn.smart.wms.module.wms.service.location;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.smart.wms.module.wms.controller.admin.location.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.location.LocationDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.location.LocationMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库位 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class LocationServiceImpl implements LocationService {

    @Resource
    private LocationMapper locationMapper;

    @Override
    public Long createLocation(LocationSaveReqVO createReqVO) {
        // 插入
        LocationDO location = BeanUtils.toBean(createReqVO, LocationDO.class);
        locationMapper.insert(location);
        // 返回
        return location.getId();
    }

    @Override
    public void updateLocation(LocationSaveReqVO updateReqVO) {
        // 校验存在
        validateLocationExists(updateReqVO.getId());
        // 更新
        LocationDO updateObj = BeanUtils.toBean(updateReqVO, LocationDO.class);
        locationMapper.updateById(updateObj);
    }

    @Override
    public void deleteLocation(Long id) {
        // 校验存在
        validateLocationExists(id);
        // 删除
        locationMapper.deleteById(id);
    }

    private void validateLocationExists(Long id) {
        if (locationMapper.selectById(id) == null) {
            throw exception(LOCATION_NOT_EXISTS);
        }
    }

    @Override
    public LocationDO getLocation(Long id) {
        return locationMapper.selectById(id);
    }

    @Override
    public PageResult<LocationDO> getLocationPage(LocationPageReqVO pageReqVO) {
        return locationMapper.selectPage(pageReqVO);
    }

}
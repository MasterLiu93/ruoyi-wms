package cn.smart.wms.module.wms.dal.mysql.location;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.location.LocationDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.location.vo.*;

/**
 * 库位 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface LocationMapper extends BaseMapperX<LocationDO> {

    default PageResult<LocationDO> selectPage(LocationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LocationDO>()
                .eqIfPresent(LocationDO::getLocationCode, reqVO.getLocationCode())
                .likeIfPresent(LocationDO::getLocationName, reqVO.getLocationName())
                .eqIfPresent(LocationDO::getRackId, reqVO.getRackId())
                .eqIfPresent(LocationDO::getLocationType, reqVO.getLocationType())
                .eqIfPresent(LocationDO::getStatus, reqVO.getStatus())
                .eqIfPresent(LocationDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(LocationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(LocationDO::getId));
    }

}
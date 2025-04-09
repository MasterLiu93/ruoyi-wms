package cn.smart.wms.module.wms.dal.mysql.warehouse;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.module.wms.controller.admin.warehouse.vo.WarehousePageReqVO;
import cn.smart.wms.module.wms.dal.dataobject.warehouse.WarehouseDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WarehouseMapper extends BaseMapperX<WarehouseDO> {

    default PageResult<WarehouseDO> selectPage(WarehousePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarehouseDO>()
                .eqIfPresent(WarehouseDO::getWarehouseCode, reqVO.getWarehouseCode())
                .likeIfPresent(WarehouseDO::getWarehouseName, reqVO.getWarehouseName())
                .eqIfPresent(WarehouseDO::getWarehouseType, reqVO.getWarehouseType())
                .eqIfPresent(WarehouseDO::getArea, reqVO.getArea())
                .eqIfPresent(WarehouseDO::getAddress, reqVO.getAddress())
                .eqIfPresent(WarehouseDO::getChargePerson, reqVO.getChargePerson())
                .eqIfPresent(WarehouseDO::getPhone, reqVO.getPhone())
                .eqIfPresent(WarehouseDO::getStatus, reqVO.getStatus())
                .eqIfPresent(WarehouseDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(WarehouseDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarehouseDO::getId));
    }
    
    /**
     * 根据仓库编码查询仓库
     *
     * @param warehouseCode 仓库编码
     * @return 仓库
     */
    default WarehouseDO selectByWarehouseCode(String warehouseCode) {
        return selectOne(WarehouseDO::getWarehouseCode, warehouseCode);
    }
}
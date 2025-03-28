package cn.smart.wms.module.wms.dal.mysql.inventorycheck;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheck.InventoryCheckDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.inventorycheck.vo.*;

/**
 * 库存盘点单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InventoryCheckMapper extends BaseMapperX<InventoryCheckDO> {

    default PageResult<InventoryCheckDO> selectPage(InventoryCheckPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InventoryCheckDO>()
                .eqIfPresent(InventoryCheckDO::getCheckNo, reqVO.getCheckNo())
                .eqIfPresent(InventoryCheckDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(InventoryCheckDO::getCheckType, reqVO.getCheckType())
                .eqIfPresent(InventoryCheckDO::getCheckStatus, reqVO.getCheckStatus())
                .eqIfPresent(InventoryCheckDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(InventoryCheckDO::getCheckedCount, reqVO.getCheckedCount())
                .eqIfPresent(InventoryCheckDO::getDifferenceCount, reqVO.getDifferenceCount())
                .eqIfPresent(InventoryCheckDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(InventoryCheckDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InventoryCheckDO::getId));
    }

}
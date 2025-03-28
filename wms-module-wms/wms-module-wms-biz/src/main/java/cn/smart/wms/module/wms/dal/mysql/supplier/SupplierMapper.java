package cn.smart.wms.module.wms.dal.mysql.supplier;

import java.util.*;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.supplier.SupplierDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.supplier.vo.*;

/**
 * 供应商 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SupplierMapper extends BaseMapperX<SupplierDO> {

    default PageResult<SupplierDO> selectPage(SupplierPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SupplierDO>()
                .eqIfPresent(SupplierDO::getSupplierCode, reqVO.getSupplierCode())
                .likeIfPresent(SupplierDO::getSupplierName, reqVO.getSupplierName())
                .eqIfPresent(SupplierDO::getSupplierLevel, reqVO.getSupplierLevel())
                .eqIfPresent(SupplierDO::getContact, reqVO.getContact())
                .eqIfPresent(SupplierDO::getPhone, reqVO.getPhone())
                .eqIfPresent(SupplierDO::getEmail, reqVO.getEmail())
                .eqIfPresent(SupplierDO::getAddress, reqVO.getAddress())
                .likeIfPresent(SupplierDO::getBankName, reqVO.getBankName())
                .eqIfPresent(SupplierDO::getBankAccount, reqVO.getBankAccount())
                .eqIfPresent(SupplierDO::getStatus, reqVO.getStatus())
                .eqIfPresent(SupplierDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(SupplierDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SupplierDO::getId));
    }

}
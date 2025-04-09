package cn.smart.wms.module.wms.dal.mysql.customer;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.smart.wms.framework.mybatis.core.mapper.BaseMapperX;
import cn.smart.wms.module.wms.dal.dataobject.customer.CustomerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.smart.wms.module.wms.controller.admin.customer.vo.*;

/**
 * 客户 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CustomerMapper extends BaseMapperX<CustomerDO> {

    default PageResult<CustomerDO> selectPage(CustomerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CustomerDO>()
                .eqIfPresent(CustomerDO::getCustomerCode, reqVO.getCustomerCode())
                .likeIfPresent(CustomerDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(CustomerDO::getCustomerLevel, reqVO.getCustomerLevel())
                .eqIfPresent(CustomerDO::getContact, reqVO.getContact())
                .eqIfPresent(CustomerDO::getPhone, reqVO.getPhone())
                .eqIfPresent(CustomerDO::getEmail, reqVO.getEmail())
                .eqIfPresent(CustomerDO::getAddress, reqVO.getAddress())
                .likeIfPresent(CustomerDO::getBankName, reqVO.getBankName())
                .eqIfPresent(CustomerDO::getBankAccount, reqVO.getBankAccount())
                .eqIfPresent(CustomerDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CustomerDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(CustomerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CustomerDO::getId));
    }
    
    /**
     * 根据客户编码查询客户
     *
     * @param customerCode 客户编码
     * @return 客户
     */
    default CustomerDO selectByCustomerCode(String customerCode) {
        return selectOne(CustomerDO::getCustomerCode, customerCode);
    }
}
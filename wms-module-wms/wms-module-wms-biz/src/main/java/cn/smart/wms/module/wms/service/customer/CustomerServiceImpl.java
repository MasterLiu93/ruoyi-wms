package cn.smart.wms.module.wms.service.customer;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.smart.wms.module.wms.controller.admin.customer.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.customer.CustomerDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.customer.CustomerMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 客户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CustomerServiceImpl implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Long createCustomer(CustomerSaveReqVO createReqVO) {
        // 校验客户编码是否唯一
        validateCustomerCodeUnique(null, createReqVO.getCustomerCode());
        
        // 插入
        CustomerDO customer = BeanUtils.toBean(createReqVO, CustomerDO.class);
        customerMapper.insert(customer);
        // 返回
        return customer.getId();
    }

    @Override
    public void updateCustomer(CustomerSaveReqVO updateReqVO) {
        // 校验存在
        validateCustomerExists(updateReqVO.getId());
        // 校验客户编码是否唯一
        validateCustomerCodeUnique(updateReqVO.getId(), updateReqVO.getCustomerCode());
        
        // 更新
        CustomerDO updateObj = BeanUtils.toBean(updateReqVO, CustomerDO.class);
        customerMapper.updateById(updateObj);
    }

    @Override
    public void deleteCustomer(Long id) {
        // 校验存在
        validateCustomerExists(id);
        // TODO 校验是否有关联的出库单等，有则不允许删除
        
        // 删除
        customerMapper.deleteById(id);
    }

    private void validateCustomerExists(Long id) {
        if (customerMapper.selectById(id) == null) {
            throw exception(CUSTOMER_NOT_EXISTS);
        }
    }
    
    /**
     * 校验客户编码是否唯一
     *
     * @param id 客户ID
     * @param customerCode 客户编码
     */
    private void validateCustomerCodeUnique(Long id, String customerCode) {
        // 如果编码为空或为空字符串，直接返回，不进行唯一性校验
        if (customerCode == null || customerCode.isEmpty()) {
            return;
        }
        
        CustomerDO customer = customerMapper.selectByCustomerCode(customerCode);
        if (customer == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的客户
        if (id == null) {
            throw exception(CUSTOMER_CODE_EXISTS);
        }
        if (!customer.getId().equals(id)) {
            throw exception(CUSTOMER_CODE_EXISTS);
        }
    }

    @Override
    public CustomerDO getCustomer(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public PageResult<CustomerDO> getCustomerPage(CustomerPageReqVO pageReqVO) {
        return customerMapper.selectPage(pageReqVO);
    }

}
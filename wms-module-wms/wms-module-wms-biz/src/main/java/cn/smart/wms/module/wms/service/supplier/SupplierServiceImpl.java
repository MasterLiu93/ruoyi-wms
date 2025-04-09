package cn.smart.wms.module.wms.service.supplier;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.smart.wms.module.wms.controller.admin.supplier.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.supplier.SupplierDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;

import cn.smart.wms.module.wms.dal.mysql.supplier.SupplierMapper;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.*;

/**
 * 供应商 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class SupplierServiceImpl implements SupplierService {

    @Resource
    private SupplierMapper supplierMapper;

    @Override
    public Long createSupplier(SupplierSaveReqVO createReqVO) {
        // 校验供应商编码是否唯一
        validateSupplierCodeUnique(null, createReqVO.getSupplierCode());
        
        // 插入
        SupplierDO supplier = BeanUtils.toBean(createReqVO, SupplierDO.class);
        supplierMapper.insert(supplier);
        // 返回
        return supplier.getId();
    }

    @Override
    public void updateSupplier(SupplierSaveReqVO updateReqVO) {
        // 校验存在
        validateSupplierExists(updateReqVO.getId());
        // 校验供应商编码是否唯一
        validateSupplierCodeUnique(updateReqVO.getId(), updateReqVO.getSupplierCode());
        
        // 更新
        SupplierDO updateObj = BeanUtils.toBean(updateReqVO, SupplierDO.class);
        supplierMapper.updateById(updateObj);
    }

    @Override
    public void deleteSupplier(Long id) {
        // 校验存在
        validateSupplierExists(id);
        // TODO 校验是否有关联的入库单等，有则不允许删除
        
        // 删除
        supplierMapper.deleteById(id);
    }

    private void validateSupplierExists(Long id) {
        if (supplierMapper.selectById(id) == null) {
            throw exception(SUPPLIER_NOT_EXISTS);
        }
    }
    
    /**
     * 校验供应商编码是否唯一
     *
     * @param id 供应商ID
     * @param supplierCode 供应商编码
     */
    private void validateSupplierCodeUnique(Long id, String supplierCode) {
        // 如果编码为空或为空字符串，直接返回，不进行唯一性校验
        if (supplierCode == null || supplierCode.isEmpty()) {
            return;
        }
        
        SupplierDO supplier = supplierMapper.selectBySupplierCode(supplierCode);
        if (supplier == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的供应商
        if (id == null) {
            throw exception(SUPPLIER_CODE_EXISTS);
        }
        if (!supplier.getId().equals(id)) {
            throw exception(SUPPLIER_CODE_EXISTS);
        }
    }

    @Override
    public SupplierDO getSupplier(Long id) {
        return supplierMapper.selectById(id);
    }

    @Override
    public PageResult<SupplierDO> getSupplierPage(SupplierPageReqVO pageReqVO) {
        return supplierMapper.selectPage(pageReqVO);
    }

}
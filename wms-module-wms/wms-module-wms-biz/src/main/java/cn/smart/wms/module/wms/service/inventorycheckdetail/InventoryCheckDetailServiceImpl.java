package cn.smart.wms.module.wms.service.inventorycheckdetail;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo.InventoryCheckDetailPageReqVO;
import cn.smart.wms.module.wms.controller.admin.inventorycheckdetail.vo.InventoryCheckDetailSaveReqVO;
import cn.smart.wms.module.wms.dal.dataobject.inventorycheckdetail.InventoryCheckDetailDO;
import cn.smart.wms.module.wms.dal.mysql.inventorycheckdetail.InventoryCheckDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.smart.wms.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.smart.wms.module.wms.enums.ErrorCodeConstants.INVENTORY_CHECK_DETAIL_NOT_EXISTS;

/**
 * 库存盘点明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class InventoryCheckDetailServiceImpl implements InventoryCheckDetailService {

    @Resource
    private InventoryCheckDetailMapper inventoryCheckDetailMapper;

    @Override
    public Long createInventoryCheckDetail(InventoryCheckDetailSaveReqVO createReqVO) {
        // 插入
        InventoryCheckDetailDO inventoryCheckDetail = BeanUtils.toBean(createReqVO, InventoryCheckDetailDO.class);
        inventoryCheckDetailMapper.insert(inventoryCheckDetail);
        // 返回
        return inventoryCheckDetail.getId();
    }

    @Override
    public void updateInventoryCheckDetail(InventoryCheckDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateInventoryCheckDetailExists(updateReqVO.getId());
        // 更新
        InventoryCheckDetailDO updateObj = BeanUtils.toBean(updateReqVO, InventoryCheckDetailDO.class);
        inventoryCheckDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteInventoryCheckDetail(Long id) {
        // 校验存在
        validateInventoryCheckDetailExists(id);
        // 删除
        inventoryCheckDetailMapper.deleteById(id);
    }

    private void validateInventoryCheckDetailExists(Long id) {
        if (inventoryCheckDetailMapper.selectById(id) == null) {
            throw exception(INVENTORY_CHECK_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public InventoryCheckDetailDO getInventoryCheckDetail(Long id) {
        return inventoryCheckDetailMapper.selectById(id);
    }

    @Override
    public PageResult<InventoryCheckDetailDO> getInventoryCheckDetailPage(InventoryCheckDetailPageReqVO pageReqVO) {
        return inventoryCheckDetailMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsertInventoryCheckDetail(List<InventoryCheckDetailDO> details) {
        if (details == null || details.isEmpty()) {
            return 0;
        }
        
        for (InventoryCheckDetailDO detail : details) {
            inventoryCheckDetailMapper.insert(detail);
        }
        
        return details.size();
    }

}
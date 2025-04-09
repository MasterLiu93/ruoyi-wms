package cn.smart.wms.framework.idgenerator.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ID生成器工厂类
 * 为各个业务模块提供统一的ID生成调用接口
 */
@Component
public class IdGeneratorFactory {

    private final RedissonIdGenerator idGenerator;

    @Autowired
    public IdGeneratorFactory(RedissonIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * 生成入库单号
     *
     * @return 入库单号
     */
    public String generateReceiptOrderNo() {
        return idGenerator.generateId("RK", 1);
    }

    /**
     * 生成出库单号
     *
     * @return 出库单号
     */
    public String generateShipmentOrderNo() {
        return idGenerator.generateId("CK", 1);
    }

    /**
     * 生成移库单号
     *
     * @return 移库单号
     */
    public String generateMoveOrderNo() {
        return idGenerator.generateId("YK", 1);
    }

    /**
     * 生成盘点单号
     *
     * @return 盘点单号
     */
    public String generateInventoryCheckNo() {
        return idGenerator.generateId("PD", 1);
    }

    /**
     * 生成批次号
     *
     * @return 批次号
     */
    public String generateBatchNo() {
        return idGenerator.generateId("PC", 1);
    }

    /**
     * 生成物料编码
     *
     * @return 物料编码
     */
    public String generateItemCode() {
        return idGenerator.generateId("WL", 1);
    }

    /**
     * 生成供应商编码
     *
     * @return 供应商编码
     */
    public String generateSupplierCode() {
        return idGenerator.generateId("GYS", 1);
    }

    /**
     * 生成客户编码
     *
     * @return 客户编码
     */
    public String generateCustomerCode() {
        return idGenerator.generateId("KH", 1);
    }

    /**
     * 生成库位编码
     * 
     * @return 库位编码
     */
    public String generateLocationCode() {
        return idGenerator.generateId("KW", 1);
    }
    
    /**
     * 生成自定义编码
     * 
     * @param prefix 编码前缀
     * @param category 类别编号
     * @return 生成的编码
     */
    public String generateCustomCode(String prefix, int category) {
        return idGenerator.generateId(prefix, category);
    }
    
    /**
     * 检查编号是否已存在
     * 
     * @param prefix 前缀
     * @param id 完整ID
     * @return 是否存在
     */
    public boolean isIdExists(String prefix, String id) {
        return idGenerator.isIdExists(prefix, id);
    }
} 
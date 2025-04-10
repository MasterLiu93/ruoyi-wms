# WMS分布式锁Starter

基于Redisson的分布式锁实现，提供简单的注解式使用方式，无需修改现有代码逻辑即可轻松集成分布式锁功能。

## 功能特性

- 注解式使用，简单便捷
- 基于Redisson实现，性能高、可靠性强
- 支持SpEL表达式定义锁的key
- 支持超时时间、等待时间等配置
- 自动释放锁，避免死锁
- 方法执行错误时自动释放锁

## 使用方法

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.smart.wms</groupId>
    <artifactId>wms-spring-boot-starter-lock</artifactId>
    <version>${revision}</version>
</dependency>
```

### 2. 在方法上添加 `@DistributedLock` 注解

```java
import cn.smart.wms.framework.lock.core.annotation.DistributedLock;

@Service
public class MyService {

    @DistributedLock(prefix = "shipment", key = "#orderId", errorMessage = "订单处理中，请稍后再试")
    public void processOrder(Long orderId) {
        // 业务逻辑
    }
}
```

### 3. SpEL表达式支持

锁的key支持SpEL表达式，例如：

```java
// 简单参数
@DistributedLock(key = "#id")
public void method1(Long id) {}

// 复杂对象属性
@DistributedLock(key = "#order.id")
public void method2(Order order) {}

// 多个参数组合
@DistributedLock(key = "#userId + ':' + #orderId")
public void method3(Long userId, Long orderId) {}

// 字符串常量组合
@DistributedLock(key = "'order:' + #orderId")
public void method4(Long orderId) {}
```

### 4. 注解参数说明

| 参数名       | 类型        | 默认值             | 说明                                     |
|------------|-----------|-----------------|----------------------------------------|
| prefix     | String    | "lock"          | 锁的key前缀，将拼接在key之前，格式为prefix:key        |
| key        | String    | 必填              | 锁的key，支持SpEL表达式                       |
| waitTime   | long      | 5               | 获取锁的最长等待时间                            |
| leaseTime  | long      | 30              | 持有锁的最长时间，超过该时间自动释放                    |
| timeUnit   | TimeUnit  | TimeUnit.SECONDS | 时间单位                                  |
| errorMessage | String  | "操作正在处理中，请稍后再试" | 获取锁失败时的错误消息                           |

## 应用场景

适用于并发访问共享资源的场景，如：

1. 库存操作：出库、入库等
2. 状态变更：订单状态流转、审批流程等
3. 数据一致性要求高的业务操作
4. 防止重复提交
5. 秒杀、限时抢购等高并发场景

## 在WMS系统中的应用

已在以下模块中使用：

1. 出库模块：`ShipmentOrderServiceImpl`
   - `executeShipment`方法：手动执行出库操作
   - `executeShipmentAfterApproval`方法：审核通过后的自动出库
   
2. 入库模块：`ReceiptOrderServiceImpl`
   - `executeReceiptAfterApproval`方法：审核通过后的自动入库
   - `executeReceipt`方法：手动执行入库操作

## 注意事项

1. 分布式锁依赖于Redis，确保Redis服务的稳定性
2. 合理设置锁的超时时间，避免长时间占用锁资源
3. 锁的粒度尽量精细，避免过度锁定影响性能
4. 方法有多个分支时，确保每个分支都能正确释放锁 
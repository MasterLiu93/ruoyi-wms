package cn.smart.wms.framework.idgenerator.autoconfigure;

import cn.smart.wms.framework.idgenerator.core.IdGeneratorFactory;
import cn.smart.wms.framework.idgenerator.core.RedissonIdGenerator;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ID生成器自动配置类
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
public class IdGeneratorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedissonIdGenerator redissonIdGenerator(RedissonClient redissonClient) {
        return new RedissonIdGenerator(redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public IdGeneratorFactory idGeneratorFactory(RedissonIdGenerator redissonIdGenerator) {
        return new IdGeneratorFactory(redissonIdGenerator);
    }
} 
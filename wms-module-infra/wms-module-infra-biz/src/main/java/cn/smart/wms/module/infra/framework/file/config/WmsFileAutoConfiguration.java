package cn.smart.wms.module.infra.framework.file.config;

import cn.smart.wms.module.infra.framework.file.core.client.FileClientFactory;
import cn.smart.wms.module.infra.framework.file.core.client.FileClientFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件配置类
 *
 * @author ljx
 */
@Configuration(proxyBeanMethods = false)
public class WmsFileAutoConfiguration {

    @Bean
    public FileClientFactory fileClientFactory() {
        return new FileClientFactoryImpl();
    }

}

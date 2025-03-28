package cn.smart.wms.module.infra.framework.web.config;

import cn.smart.wms.framework.swagger.config.WmsSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * infra 模块的 web 组件的 Configuration
 *
 * @author ljx
 */
@Configuration(proxyBeanMethods = false)
public class InfraWebConfiguration {

    /**
     * infra 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi infraGroupedOpenApi() {
        return WmsSwaggerAutoConfiguration.buildGroupedOpenApi("infra");
    }

}

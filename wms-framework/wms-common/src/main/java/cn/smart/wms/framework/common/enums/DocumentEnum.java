package cn.smart.wms.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档地址
 *
 * @author ljx
 */
@Getter
@AllArgsConstructor
public enum DocumentEnum {

    REDIS_INSTALL("https://gitee.com/zhijiantianya/ruoyi-vue-pro/issues/I4VCSJ", "Redis 安装文档"),
    TENANT("https://doc.iocoder.cn", "SaaS 多租户文档");

    private final String url;
    private final String memo;

}

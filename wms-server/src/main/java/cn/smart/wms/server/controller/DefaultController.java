package cn.smart.wms.server.controller;

import cn.smart.wms.framework.common.pojo.CommonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.smart.wms.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_IMPLEMENTED;

/**
 * 默认 Controller，解决部分 module 未开启时的 404 提示。
 * 例如说，/bpm/** 路径，工作流
 *
 * @author ljx
 */
@RestController
public class DefaultController {

/*    @RequestMapping("/admin-api/bpm/**")
    public CommonResult<Boolean> bpm404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[工作流模块 wms-module-bpm - 已禁用][参考 https://doc.iocoder.cn/bpm/ 开启]");
    }

    @RequestMapping("/admin-api/mp/**")
    public CommonResult<Boolean> mp404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[微信公众号 wms-module-mp - 已禁用][参考 https://doc.iocoder.cn/mp/build/ 开启]");
    }

    @RequestMapping(value = {"/admin-api/product/**", // 商品中心
            "/admin-api/trade/**", // 交易中心
            "/admin-api/promotion/**"})  // 营销中心
    public CommonResult<Boolean> mall404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[商城系统 wms-module-mall - 已禁用][参考 https://doc.iocoder.cn/mall/build/ 开启]");
    }

    @RequestMapping("/admin-api/erp/**")
    public CommonResult<Boolean> erp404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[ERP 模块 wms-module-erp - 已禁用][参考 https://doc.iocoder.cn/erp/build/ 开启]");
    }

    @RequestMapping("/admin-api/crm/**")
    public CommonResult<Boolean> crm404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[CRM 模块 wms-module-crm - 已禁用][参考 https://doc.iocoder.cn/crm/build/ 开启]");
    }

    @RequestMapping(value = {"/admin-api/report/**"})
    public CommonResult<Boolean> report404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[报表模块 wms-module-report - 已禁用][参考 https://doc.iocoder.cn/report/ 开启]");
    }

    @RequestMapping(value = {"/admin-api/pay/**"})
    public CommonResult<Boolean> pay404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[支付模块 wms-module-pay - 已禁用][参考 https://doc.iocoder.cn/pay/build/ 开启]");
    }

    @RequestMapping(value = {"/admin-api/ai/**"})
    public CommonResult<Boolean> ai404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[AI 大模型 wms-module-ai - 已禁用][参考 https://doc.iocoder.cn/ai/build/ 开启]");
    }

    @RequestMapping(value = {"/admin-api/iot/**"})
    public CommonResult<Boolean> iot404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "[IOT 物联网 wms-module-iot - 已禁用][参考 https://doc.iocoder.cn/iot/build/ 开启]");
    }*/

}

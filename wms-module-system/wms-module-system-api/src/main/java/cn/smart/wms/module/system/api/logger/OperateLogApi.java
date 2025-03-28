package cn.smart.wms.module.system.api.logger;

import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.module.system.api.logger.dto.OperateLogCreateReqDTO;
import cn.smart.wms.module.system.api.logger.dto.OperateLogPageReqDTO;
import cn.smart.wms.module.system.api.logger.dto.OperateLogRespDTO;
import org.springframework.scheduling.annotation.Async;

import javax.validation.Valid;

/**
 * 操作日志 API 接口
 *
 * @author ljx
 */
public interface OperateLogApi {

    /**
     * 创建操作日志
     *
     * @param createReqDTO 请求
     */
    void createOperateLog(@Valid OperateLogCreateReqDTO createReqDTO);

    /**
     * 【异步】创建操作日志
     *
     * @param createReqDTO 请求
     */
    @Async
    default void createOperateLogAsync(OperateLogCreateReqDTO createReqDTO) {
        createOperateLog(createReqDTO);
    }

    /**
     * 获取指定模块的指定数据的操作日志分页
     *
     * @param pageReqDTO 请求
     * @return 操作日志分页
     */
    PageResult<OperateLogRespDTO> getOperateLogPage(OperateLogPageReqDTO pageReqDTO);

}

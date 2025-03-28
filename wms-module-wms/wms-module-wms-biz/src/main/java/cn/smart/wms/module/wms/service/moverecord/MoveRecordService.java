package cn.smart.wms.module.wms.service.moverecord;

import java.util.*;
import javax.validation.*;
import cn.smart.wms.module.wms.controller.admin.moverecord.vo.*;
import cn.smart.wms.module.wms.dal.dataobject.moverecord.MoveRecordDO;
import cn.smart.wms.framework.common.pojo.PageResult;
import cn.smart.wms.framework.common.pojo.PageParam;

/**
 * 移库操作记录 Service 接口
 *
 * @author 芋道源码
 */
public interface MoveRecordService {

    /**
     * 创建移库操作记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMoveRecord(@Valid MoveRecordSaveReqVO createReqVO);

    /**
     * 更新移库操作记录
     *
     * @param updateReqVO 更新信息
     */
    void updateMoveRecord(@Valid MoveRecordSaveReqVO updateReqVO);

    /**
     * 删除移库操作记录
     *
     * @param id 编号
     */
    void deleteMoveRecord(Long id);

    /**
     * 获得移库操作记录
     *
     * @param id 编号
     * @return 移库操作记录
     */
    MoveRecordDO getMoveRecord(Long id);

    /**
     * 获得移库操作记录分页
     *
     * @param pageReqVO 分页查询
     * @return 移库操作记录分页
     */
    PageResult<MoveRecordDO> getMoveRecordPage(MoveRecordPageReqVO pageReqVO);

}
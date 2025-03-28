package cn.smart.wms.module.system.api.dept;

import cn.smart.wms.framework.common.util.object.BeanUtils;
import cn.smart.wms.module.system.api.dept.dto.PostRespDTO;
import cn.smart.wms.module.system.dal.dataobject.dept.PostDO;
import cn.smart.wms.module.system.service.dept.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 岗位 API 实现类
 *
 * @author ljx
 */
@Service
public class PostApiImpl implements PostApi {

    @Resource
    private PostService postService;

    @Override
    public void validPostList(Collection<Long> ids) {
        postService.validatePostList(ids);
    }

    @Override
    public List<PostRespDTO> getPostList(Collection<Long> ids) {
        List<PostDO> list = postService.getPostList(ids);
        return BeanUtils.toBean(list, PostRespDTO.class);
    }

}

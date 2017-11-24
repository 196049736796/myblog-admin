package cn.myxinge.service;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.BoardMsg;
import cn.myxinge.entity.VisitIp;
import org.springframework.data.domain.Page;

/**
 * Created by chenxinghua on 2017/11/23.
 */
public interface VisitIpService {
    //存储
    void save(VisitIp visitIp);

    //留言读取
    Page<VisitIp> list(Integer page, Integer rows);

    Long getCount(VisitIp o);

    void delete(Integer id);
}

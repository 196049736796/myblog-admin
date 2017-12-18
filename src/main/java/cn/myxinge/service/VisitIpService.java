package cn.myxinge.service;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.BoardMsg;
import cn.myxinge.entity.VisitIp;
import org.springframework.data.domain.Page;

/**
 * Created by chenxinghua on 2017/11/23.
 */
public interface VisitIpService extends BaseService<VisitIp> {
    VisitIp findByIP(VisitIp visitIps);
    String ipSave(VisitIp visitIp);
}

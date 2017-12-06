package cn.myxinge.service.impl;

import cn.myxinge.dao.ResourceDao;
import cn.myxinge.dao.VisitIpDao;
import cn.myxinge.entity.VisitIp;
import cn.myxinge.service.VisitIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by chenxinghua on 2017/11/23.
 */
@Service
@Transactional
public class VisitIpServiceImpl extends BaseServiceImpl<VisitIp> implements VisitIpService {
    private VisitIpDao visitIpDao;
    @Override
    public VisitIp findByIP(VisitIp visitIp) {
        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("ip",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");
        Example<VisitIp> ex = Example.of(visitIp, ma);

        List<VisitIp> all = visitIpDao.findAll(ex);
        return all.size() > 0 ? all.get(0) : null;
    }

    @Autowired
    public void setBlogDao(VisitIpDao visitIpDao) {
        this.visitIpDao = visitIpDao;
        super.setJpaRepository(visitIpDao);
    }
}

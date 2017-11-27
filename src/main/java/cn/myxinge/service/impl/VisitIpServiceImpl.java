package cn.myxinge.service.impl;

import cn.myxinge.dao.VisitIpDao;
import cn.myxinge.entity.Blog;
import cn.myxinge.entity.VisitIp;
import cn.myxinge.service.VisitIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by chenxinghua on 2017/11/23.
 */
@Service
@Transactional
public class VisitIpServiceImpl implements VisitIpService {
    @Autowired
    private VisitIpDao visitIpDao;
    @Override
    public void save(VisitIp visitIp) {

        //查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("ip", ExampleMatcher.GenericPropertyMatchers.caseSensitive());
        VisitIp example = new VisitIp();
        example.setIp(visitIp.getIp());

        List<VisitIp> all = visitIpDao.findAll(Example.of(example, matcher));
        if(all.size() == 1){
            VisitIp visitIp1 = all.get(0);
            Long visitNum = visitIp1.getVisitNum();
            if(null == visitNum){visitNum = 0L;}
            visitIp1.setVisitNum(visitNum+ 1);
            visitIp1.setVisittime(visitIp.getVisittime());
            visitIpDao.save(visitIp1);
            return;
        }
        visitIp.setVisitNum(1L);
        visitIpDao.save(visitIp);
    }

    @Override
    public Page<VisitIp> list(Integer page, Integer rows) {
        Sort sort = new Sort(Sort.Direction.DESC, "visittime");
        Pageable pageable = new PageRequest(page - 1, rows,sort);
        return visitIpDao.findAll(pageable);
    }

    @Override
    public Long getCount(VisitIp o) {
        return visitIpDao.count();
    }

    @Override
    public void delete(Integer id) {
        visitIpDao.delete(id);
    }

    @Override
    public VisitIp findByIP(VisitIp visitIp) {
        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("ip",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");
        Example<VisitIp> ex = Example.of(visitIp, ma);

        List<VisitIp> all = visitIpDao.findAll(ex);
        return all.size() > 0 ? all.get(0) : null;
    }

    @Override
    public void update(VisitIp visitIp1) {
        visitIpDao.save(visitIp1);
    }
}

package cn.myxinge.service.impl;

import cn.myxinge.dao.VisitIpDao;
import cn.myxinge.entity.Blog;
import cn.myxinge.entity.BoardMsg;
import cn.myxinge.entity.VisitIp;
import cn.myxinge.service.VisitIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}

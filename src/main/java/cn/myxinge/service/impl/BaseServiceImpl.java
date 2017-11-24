package cn.myxinge.service.impl;

import cn.myxinge.dao.BaseDao;
import cn.myxinge.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by XingChen on 2017/11/19.
 */
/*@Service
@Transactional*/
public class BaseServiceImpl<T> implements BaseService {
    //todo
    @Autowired
//    private BaseDao baseDao;
    @Override
    public Page list(Object add, Object update, Integer page, Integer rows) {
//        Sort sort = new Sort(Sort.Direction.DESC,"createtime");
        int firstResult = (page - 1) * rows;
        Pageable pageable = new PageRequest(firstResult, rows);
        return null;
    }

    @Override
    public Long getCount(Object o) {
        //todo 条件查询未完成
        return null;
    }
}





















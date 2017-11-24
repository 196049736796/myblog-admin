package cn.myxinge.service.impl;

import cn.myxinge.dao.BoardMsgDao;
import cn.myxinge.entity.Blog;
import cn.myxinge.entity.BoardMsg;
import cn.myxinge.service.BoardMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by XingChen on 2017/11/7.
 */
@Service
@Transactional
public class BoardMsgServiceImpl implements BoardMsgService {

    @Autowired
    private BoardMsgDao boardMsgDao;

    @Override
    public void save(BoardMsg msg) {
        boardMsgDao.save(msg);
    }

    @Override
    public Page<BoardMsg> list(Integer page, Integer rows) {
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        Pageable pageable = new PageRequest(page - 1, rows,sort);
        return boardMsgDao.findAll(pageable);
    }
    @Override
    public Long getCount(Blog o) {
        //todo 条件查询未完成
        return boardMsgDao.count();
    }

}

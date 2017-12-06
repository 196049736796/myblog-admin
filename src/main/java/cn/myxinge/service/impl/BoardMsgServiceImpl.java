package cn.myxinge.service.impl;

import cn.myxinge.dao.BlogDao;
import cn.myxinge.dao.BoardMsgDao;
import cn.myxinge.entity.BoardMsg;
import cn.myxinge.service.BoardMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by XingChen on 2017/11/7.
 */
@Service
@Transactional
public class BoardMsgServiceImpl extends BaseServiceImpl<BoardMsg> implements BoardMsgService {

    private BoardMsgDao boardMsgDao;

    @Autowired
    public void setBlogDao(BoardMsgDao boardMsgDao) {
        this.boardMsgDao = boardMsgDao;
        super.setJpaRepository(boardMsgDao);
    }
}

package cn.myxinge.service.impl;

import cn.myxinge.dao.CommentsDao;
import cn.myxinge.dao.MenuDao;
import cn.myxinge.entity.Comments;
import cn.myxinge.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Created by chenxinghua on 2017/12/27.
 */
@Service
public class CommentsServiceImpl extends BaseServiceImpl<Comments> implements CommentsService {

    @Autowired
    private CommentsDao commentsDao;

    @Autowired
    public void setBlogDao(CommentsDao commentsDao) {
        this.commentsDao = commentsDao;
        super.setJpaRepository(commentsDao);
    }
}

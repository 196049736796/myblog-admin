package cn.myxinge.service;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.BoardMsg;
import org.springframework.data.domain.Page;

/**
 * Created by XingChen on 2017/11/7.
 */
public interface BoardMsgService {
    //存储
    void save(BoardMsg msg);

    //留言读取
    Page<BoardMsg> list(Integer page, Integer rows);

    Long getCount(Blog o);
}

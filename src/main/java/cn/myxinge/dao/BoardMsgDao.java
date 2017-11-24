package cn.myxinge.dao;

import cn.myxinge.entity.BoardMsg;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by XingChen on 2017/11/7.
 */
public interface BoardMsgDao extends JpaRepository<BoardMsg, Integer> {
}

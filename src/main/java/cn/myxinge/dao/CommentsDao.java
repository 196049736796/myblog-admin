package cn.myxinge.dao;

import cn.myxinge.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chenxinghua on 2017/12/27.
 */
public interface CommentsDao extends JpaRepository<Comments, Integer> {
}

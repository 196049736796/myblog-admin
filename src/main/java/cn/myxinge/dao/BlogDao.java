package cn.myxinge.dao;

import cn.myxinge.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chenxinghua on 2017/11/9.
 */
public interface BlogDao extends JpaRepository<Blog, Integer> {
}

package cn.myxinge.dao;

import cn.myxinge.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by chenxinghua on 2017/11/9.
 */
public interface BlogDao extends JpaRepository<Blog, Integer> {

    @Query(value = "select * from Blog b where  b.createtime< ? limit 1", nativeQuery = true)
    public Blog preBlog(Date curBlogCreateTime);

    @Query(value = "select * from Blog b where  b.createtime> ? limit 1", nativeQuery = true)
    public Blog nextBlog(Date curBlogCreateTime);
}

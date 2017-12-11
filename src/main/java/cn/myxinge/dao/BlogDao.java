package cn.myxinge.dao;

import cn.myxinge.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by chenxinghua on 2017/11/9.
 */
public interface BlogDao extends JpaRepository<Blog, Integer> {

    @Query(value = "select * from blog b where  b.createtime< ? limit 1", nativeQuery = true)
    public Blog preBlog(Date curBlogCreateTime);

    @Query(value = "select * from blog b where  b.createtime> ? limit 1", nativeQuery = true)
    public Blog nextBlog(Date curBlogCreateTime);

    @Query(value = "SELECT * from blog GROUP BY MONTH(createTime) ORDER BY createTime DESC;", nativeQuery = true)
    public List<Blog> archives();
}

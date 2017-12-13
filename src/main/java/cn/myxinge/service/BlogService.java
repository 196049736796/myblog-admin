package cn.myxinge.service;

import cn.myxinge.entity.Blog;

import java.util.List;
import java.util.Map;

/**
 * Created by chenxinghua on 2017/11/9.
 */
public interface BlogService extends BaseService<Blog>{
    Blog getBlogByUrl(String url);

    Map<String,Blog> findPreAndNext(Blog blog);

    List listByArchives();
}

















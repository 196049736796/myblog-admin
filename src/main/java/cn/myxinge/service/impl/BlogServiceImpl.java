package cn.myxinge.service.impl;

import cn.myxinge.dao.BlogDao;
import cn.myxinge.dao.ResourceDao;
import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.BlogService;
import cn.myxinge.utils.FastDFSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxinghua on 2017/11/9.
 */
@Service
@Transactional
public class BlogServiceImpl extends BaseServiceImpl<Blog> implements BlogService {
    private static final Logger LOG = LoggerFactory.getLogger(BlogServiceImpl.class);
    private BlogDao blogDao;
    @Autowired
    private ResourceDao resourceDao;

    @Override
//    @Cacheable
    public Blog getBlogByUrl(String url) {
        Blog b = new Blog();
        b.setUrl(url);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("url", ExampleMatcher.GenericPropertyMatchers.caseSensitive());

        List<Blog> list = blogDao.findAll(Example.of(b, matcher));

        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public void update(Blog blog) {
        blog.setUpdatetime(new Date());
        super.update(blog);
    }

    @Override
    public Map<String, Blog> findPreAndNext(Blog blog) {
        //HashMap即是value为null也不会抛出异常
        Map<String,Blog> map = new HashMap<String,Blog>();
        map.put("preBlog", blogDao.preBlog(blog.getCreatetime()));
        map.put("nextBlog", blogDao.nextBlog(blog.getCreatetime()));
        return map;
    }

    @Autowired
    public void setBlogDao(BlogDao blogDao) {
        this.blogDao = blogDao;
        super.setJpaRepository(blogDao);
    }
}













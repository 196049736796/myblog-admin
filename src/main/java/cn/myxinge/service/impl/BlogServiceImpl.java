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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Date;
import java.util.List;

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

//    @Override
//    public Blog getBlog(String url) {
//
//        Blog b = new Blog();
//        b.setUrl(url);
//
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withMatcher("url", ExampleMatcher.GenericPropertyMatchers.caseSensitive());
//
//        List<Blog> list = blogDao.findAll(Example.of(b, matcher));
//
//        return list.size() > 0 ? list.get(0) : null;
//    }
//
//    @Override
//    public List<Blog> allBlog() {
//        return blogDao.findAll();
//    }
//
//    @Override
//    public Page<Blog> newerBlog(int n) {
//        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
//
//        //状态判断 : 状态为1是才显示
//        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("state",
//                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
//                .withIgnorePaths("focus");
//        Blog blog = new Blog();
//        blog.setState(Blog.STATE_ONLINE);
//        Example<Blog> ex = Example.of(blog, ma);
//
//        Pageable pageable = new PageRequest(0, n, sort);
//        return blogDao.findAll(ex, pageable);
//    }

//    @Override
//    public Page list(Blog b, Integer page, Integer rows) {
//        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
//        int firstResult = (page - 1) * rows;
//        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("state",
//                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
//                .withIgnorePaths("focus");
//        Blog blog = new Blog();
//        blog.setState(Blog.STATE_ONLINE);
//        Example<Blog> ex = Example.of(blog, ma);
//        Pageable pageable = new PageRequest(firstResult, rows, sort);
//        return blogDao.findAll(ex, pageable);
//    }

    /**
     * 返回所有 不考虑状态
     *
     * @param add
     * @param update
     * @param page
     * @param rows
     * @return
     */
//    @Override
//    public Page listWithoutState(Blog add, Blog update, Integer page, Integer rows) {
//        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
//        int firstResult = (page - 1) * rows;
//        Pageable pageable = new PageRequest(firstResult, rows, sort);
//        return blogDao.findAll(pageable);
//    }

//    @Override
//    public Long getCount(Blog o) {
//        //todo 条件查询未完成
//        return blogDao.count();
//    }

    /**
     * 博客上传存储(包括大图)
     *
     * @param html
     * @param blog
     */
//    @Override
//    public int addBlog(Blog blog) {
//        blog.setAuth(StringUtils.isEmpty(blog.getAuth()) ? "Xingchen" : blog.getAuth());
//        if (null == blog.getCreatetime()) {
//            blog.setCreatetime(new Date());
//            blog.setUpdatetime(null);
//        }
//        blog.setState(Blog.STATE_OFFLINE);
//        blogDao.save(blog);
//        return 0;
//    }

    /**
     * 通过ID查询博客
     *
     * @param id
     * @return
     */
//    @Override
//    public Blog getBlogById(Integer id) {
//        return blogDao.findOne(id);
//    }
//
//    /**
//     * 删除博客
//     */
//    @Override
//    public void delete(Integer id) {
//        blogDao.delete(id);
//    }
//
//
//    @Override
//    public void save(Blog blog) {
//        blogDao.save(blog);
//    }

    @Autowired
    public void setBlogDao(BlogDao blogDao) {
        System.out.println(blogDao);
        this.blogDao = blogDao;
        super.setJpaRepository(blogDao);
    }


}













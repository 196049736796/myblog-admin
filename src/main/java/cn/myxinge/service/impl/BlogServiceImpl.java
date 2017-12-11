package cn.myxinge.service.impl;

import cn.myxinge.dao.BlogDao;
import cn.myxinge.dao.ResourceDao;
import cn.myxinge.entity.Blog;
import cn.myxinge.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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
        Map<String, Blog> map = new HashMap<String, Blog>();
        map.put("preBlog", blogDao.preBlog(blog.getCreatetime()));
        map.put("nextBlog", blogDao.nextBlog(blog.getCreatetime()));
        return map;
    }

    @Override
    public Map<String, List<Blog>> listByArchives() {
        List<Blog> all = listAll();//所有数据，上线的

        //todo
        Map<String,List<Blog>> rtn = new HashMap<>();
        int year = all.get(0).getCreatetime().getYear();
        int curYear = new Date().getYear();

        for(;year <= curYear;year++){
            List<Blog> yearData = yearData(year, all);
            rtn.put(String.valueOf(year),yearData);
        }

        return rtn;
    }


    //返回所有已上线的数据/无分页
    public List<Blog> listAll() {
        Blog b = new Blog();
        b.setState(Blog.STATE_ONLINE);

        Sort sort = new Sort(Sort.Direction.ASC, "createtime");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("state", ExampleMatcher.GenericPropertyMatchers.caseSensitive());

        List<Blog> list = blogDao.findAll(Example.of(b, matcher),sort);
        return list;
    }

    private List<Blog> yearData(int year,List<Blog> blogs){
        List<Blog> yearB = new ArrayList<>();
        for(Blog b: blogs){
            if(b.getCreatetime().getYear() == year){
                yearB.add(b);
            }
        }
        //todo 循环月份

        return null;
    }

    @Autowired
    public void setBlogDao(BlogDao blogDao) {
        this.blogDao = blogDao;
        super.setJpaRepository(blogDao);
    }
}













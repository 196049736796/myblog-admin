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
    public void update(Blog blog) throws Exception {
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
    public List listByArchives() {
        List<Blog> all = listAll();//所有数据，上线的

        if (null == all || all.size() < 1) {
            return null;
        }

        Map<String, List<Blog>> map = null;
        List<Map<String, List<Blog>>> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(all.get(0).getCreatetime());
        int y = calendar.get(Calendar.YEAR);
        calendar.setTime(all.get(all.size() - 1).getCreatetime());
        int _y = calendar.get(Calendar.YEAR);

        for (; y >= _y; y--) {
            for (int i = 12; i >= 1; i--) {
                List<Blog> l = blogByMonth(y, i, all);
                if (null != l && l.size() > 0) {
                    map = new HashMap<>();
                    map.put(y + "-" + i, l);
                    list.add(map);
                }
            }
        }

        return list;
    }


    private List<Blog> blogByMonth(int year, int mongth, List<Blog> blogs) {
        List<Blog> rtn = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Blog b : blogs) {
            calendar.setTime(b.getCreatetime());
            if (year == calendar.get(Calendar.YEAR) && mongth == calendar.get(Calendar.MONTH) + 1) {
                rtn.add(b);
            }
        }
        return rtn;
    }


    //返回所有已上线的数据/无分页
    public List<Blog> listAll() {
        Blog b = new Blog();
        b.setState(Blog.STATE_ONLINE);

        Sort sort = new Sort(Sort.Direction.DESC, "createtime");

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("state", ExampleMatcher.GenericPropertyMatchers.caseSensitive());

        List<Blog> list = blogDao.findAll(Example.of(b, matcher), sort);
        return list;
    }

    @Autowired
    public void setBlogDao(BlogDao blogDao) {
        this.blogDao = blogDao;
        super.setJpaRepository(blogDao);
    }
}













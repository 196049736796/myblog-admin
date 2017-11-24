package cn.myxinge.service.impl;

import cn.myxinge.common.WriteMyLog;
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
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by chenxinghua on 2017/11/9.
 */
@Service
@Transactional
public class BlogServiceImpl implements BlogService {
    private static final Logger LOG = LoggerFactory.getLogger(BlogServiceImpl.class);
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private ResourceDao resourceDao;

    @Override
    public void create(Blog blog) {
        blogDao.save(blog);
    }

    @Override
    public Blog getBlog(String url) {

        Blog b = new Blog();
        b.setUrl(url);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("url", ExampleMatcher.GenericPropertyMatchers.caseSensitive());

        List<Blog> list = blogDao.findAll(Example.of(b, matcher));

        return list.size() > 0 ? list.get(0) : null;
    }

    @Override
    public List<Blog> allBlog() {
        return blogDao.findAll();
    }

    @Override
    public Page<Blog> newerBlog(int n) {
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");

        //状态判断 : 状态为1是才显示
        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("state",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");
        Blog blog = new Blog();
        blog.setState(Blog.STATE_ONLINE);
        Example<Blog> ex = Example.of(blog, ma);

        Pageable pageable = new PageRequest(0, n, sort);
        return blogDao.findAll(ex,pageable);
    }

    @Override
    public Page list(Blog add, Blog update, Integer page, Integer rows) {
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        int firstResult = (page - 1) * rows;
        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("state",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");
        Blog blog = new Blog();
        blog.setState(Blog.STATE_ONLINE);
        Example<Blog> ex = Example.of(blog, ma);
        Pageable pageable = new PageRequest(firstResult, rows,sort);
        return blogDao.findAll(ex,pageable);
    }

    /**
     * 返回所有 不考虑状态
     * @param add
     * @param update
     * @param page
     * @param rows
     * @return
     */
    @Override
    public Page listWithoutState(Blog add, Blog update, Integer page, Integer rows) {
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        int firstResult = (page - 1) * rows;
        Pageable pageable = new PageRequest(firstResult, rows,sort);
        return blogDao.findAll(pageable);
    }

    @Override
    public Long getCount(Blog o) {
        //todo 条件查询未完成
        return blogDao.count();
    }

    /**
     * 博客页面上传存储
     * @param html
     * @param blog
     */
    @Override
    public int addBlog(MultipartFile html, Blog blog) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String rootPath = this.getClass().getResource("/").getPath();
        String filePath = null;

        // 上传文件
        try {
            is = html.getInputStream();
            isr = new InputStreamReader(is);// 字符流
            br = new BufferedReader(isr);// 缓冲流
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            //读取配置文件，更新html文件
            Properties pro = new Properties();
            pro.load(new FileInputStream(rootPath + "static/conf/html.properties"));
            String html1 = pro.getProperty("html");

            //找到</body>节点
            String html_str = sb.toString();
            int i = html_str.indexOf("</body>");
            String start = html_str.substring(0, i);
            String end = html_str.substring(i);

            //结果
            String result = start.concat(html1).concat(end);

            filePath = rootPath + "static/temp/";
            File temp = new File(filePath);
            if (!temp.exists()) {
                temp.mkdirs();
            }
            //写出
            Writer writer = new FileWriter(filePath + html.getOriginalFilename());
            writer.write(result);
            writer.close();


            //上传
            FastDFSClient fastDFSClient = null;
            fastDFSClient = new FastDFSClient(rootPath + "static/conf/client.conf");
            int dian = html.getOriginalFilename().indexOf(".");
            String sysyUrl = fastDFSClient.uploadFile(filePath + html.getOriginalFilename(),
                    html.getOriginalFilename().substring(dian + 1));

            //存储数据库
            blog.setAuth("Xingchen");
            if(null == blog.getCreatetime()){
                blog.setCreatetime(new Date());
                blog.setUpdatetime(null);
            }
            blog.setState(Blog.STATE_OFFLINE);
            blog.setSysyUrl(sysyUrl);
            blogDao.save(blog);

            //存储资源表
            Resource resource = new Resource();
            resource.setDescription("页面Html");
            resource.setSuffix("html");
            resource.setSysyUrl(sysyUrl);
            resource.setBlogid(blog.getId());
            resourceDao.save(resource);

        } catch (Exception e) {
            LOG.error("博客上传异常：" + e.getCause());
            return -1;
        } finally {

            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                LOG.error("博客上传异常：" + e.getCause());
            }

            //删除临时文件
            File file = new File(filePath + html.getOriginalFilename());
            if (file != null) {
                file.delete();
            }
        }

        return 0;
    }

    /**
     * 通过ID查询博客
     * @param id
     * @return
     */
    @Override
    public Blog getBlogById(Integer id) {
        return blogDao.findOne(id);
    }

    /**
     * 删除博客
     */
    @Override
    public void delete(Integer id) {
        blogDao.delete(id);
    }


    @Override
    public void save(Blog blog) {
        blogDao.save(blog);
    }
}













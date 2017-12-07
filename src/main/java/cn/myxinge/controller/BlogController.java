package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.BaseService;
import cn.myxinge.service.BlogService;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.FastDFSClient;
import cn.myxinge.utils.FileUtil;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.JSONPath;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenxinghua on 2017/11/9.
 * <p>
 * 博客管理控制层
 */
@RestController
@RequestMapping("/admin/blog")
public class BlogController extends BaseController<Blog> {
    private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);
    private BlogService blogService;
    @Autowired
    private ResourceService resourceService;
    @Value("${baseUrl}")
    private String baseUrl;


    @Override
    @RequestMapping("/list")
    public Map list(Blog t, Integer page, Integer rows) {
        return super.list(t, page, rows);
    }

    @RequestMapping(value = "/listOnLine", method = {RequestMethod.GET, RequestMethod.POST})
    public Map listOnLine(Blog t, Integer page, Integer rows) {
        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("state",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");
        Blog blog = new Blog();
        blog.setState(Blog.STATE_ONLINE);
        Example<Blog> ex = Example.of(blog, ma);
        Page<Blog> data = blogService.listOnWhere(t, page, rows, getSort(), ex);
        long total = blogService.getCount(null);
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());
        return mapData;
    }


    /**
     * 根据博客url获取博客内容
     *
     * @param url
     * @param model
     * @return
     */
    @RequestMapping(value = "/{url}", method = {RequestMethod.GET})
    public Blog showBlog(@PathVariable String url) {
        Blog blog = blogService.getBlogByUrl(url);
        return blog;
    }


    //跟据id删除 -- 带资源
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public JSONObject delete(Integer id, String isDelre) throws Exception {
        if (null == id) {
            return ResponseUtil.returnJson(false, "ID为空");
        }

        super.delete(id);
//todo 后期添加功能：如果页面选择 同时删除博客删除关联资源，才会删除资源。默认删除
//        if (!"1".equals(isDelre)) {
//            return ResponseUtil.returnJson(true, "success");
//        }

        Resource resource = new Resource();
        resource.setBlogid(id);
        String result = resourceService.deleteWithBlog(resource);

        if ("success".equals(result)) {
            blogService.delete(id);
            return ResponseUtil.returnJson(true, "成功");
        }

        return ResponseUtil.returnJson(false, result);
    }


    /**
     * 根据主键查询
     */
    @RequestMapping(value = "/findById/{id}", method = {RequestMethod.GET})
    public Blog findById(@PathVariable Integer id) {
        if (null == id) {
            return null;
        }
        return blogService.getById(id);
    }


    /**
     * 上线下线
     */
    @RequestMapping(value = "/changState/{id}", method = {RequestMethod.PUT})
    public JSONObject changState(@PathVariable Integer id) {
        if (null != id) {
            Blog blogById = blogService.getById(id);
            Integer state = blogById.getState();

            if (Blog.STATE_OFFLINE == state.intValue()) {
                blogById.setState(Blog.STATE_ONLINE);
            } else if (Blog.STATE_ONLINE == state.intValue()) {
                blogById.setState(Blog.STATE_OFFLINE);
            } else {
                return ResponseUtil.returnJson(false, "失败,状态未知");
            }

            blogService.update(blogById);
            return ResponseUtil.returnJson(true, "成功");
        }

        return ResponseUtil.returnJson(false, "失败,博客不存在");
    }


    /**
     * 添加/修改
     */
    @RequestMapping("/saveWithoutHtml")
    public JSONObject add(Blog blog, MultipartFile mainImg) {
        if (null != blog.getId()) {
            //此时标识数据更新，而不是添加
            Blog syBlog = blogService.getById(blog.getId());
            blog.setCreatetime(syBlog.getCreatetime());
            blog.setSysyUrl(syBlog.getSysyUrl());
            blog.setMainImgUrl(syBlog.getMainImgUrl());
        }
        //处理md文件上传逻辑
        blog.setState(Blog.STATE_OFFLINE);
        blog.setAuth("Xingchen");
        String rtn = super.add(blog);   //注：虽然方法名叫做add,但当数据有id时，表明是更新，执行的是update
        if ("1".equals(rtn)) {
            try {
                if (null != mainImg && !StringUtils.isEmpty(mainImg.getOriginalFilename())) {
                    String imgUrl = resourceService.upload(mainImg.getInputStream(), doResource(blog.getId(), mainImg.getOriginalFilename()));
                    if (!"-1".equals(imgUrl)) {
                        //删除原有的img
                        if (null != blog.getMainImgUrl()) {
                            Resource r = new Resource();
                            r.setSysyUrl(blog.getMainImgUrl());
                            resourceService.deleteSysFile(r);
                        }
                        blog.setMainImgUrl(imgUrl);
                        super.update(blog);
                    } else {
                        return ResponseUtil.returnJson(false, "上传失败");
                    }
                }
                JSONObject json = ResponseUtil.returnJson(true, "保存成功");
                Object eval = JSONPath.eval(json, "$.success");
                JSONPath.set(json, "$.id", blog.getId());
                return json;

            } catch (Exception e) {
                LOG.error("上传或删除源文件·失败", e);
                return ResponseUtil.returnJson(false, "上传或删除源文件·失败，发生异常");
            }
        } else {
            return ResponseUtil.returnJson(false, "存储失败");
        }
    }

    /**
     * 添加、更新 md文件
     */
    @RequestMapping("/saveHtml")
    public JSONObject add(Integer id, String md) {
        Blog blog = blogService.getById(id);
        String sysyUrl = null;
        try {
            sysyUrl = resourceService.upload(
                    new ByteArrayInputStream(md.getBytes("utf-8")), doResource(blog.getId(), blog.getTitle().concat(".md")));
            if (null != blog.getSysyUrl()) {
                Resource r = new Resource();
                r.setSysyUrl(blog.getSysyUrl());
                resourceService.deleteSysFile(r);
            }
        } catch (Exception e) {
            LOG.error("上传失败", e);
            return ResponseUtil.returnJson(false, "上传失败，发生异常");
        }
        if (!"-1".equals(sysyUrl)) {
            blog.setSysyUrl(sysyUrl);
            blog.setCreatetime(blog.getCreatetime() == null ? new Date() : blog.getCreatetime());
            super.update(blog);
            return ResponseUtil.returnJson(false, "上传成功");
        } else {
            return ResponseUtil.returnJson(false, "上传失败");
        }
    }

    //获取md文本
    @RequestMapping("/getMd/{id}")
    public String getMd(@PathVariable Integer id) {
        try {
            Blog blog = blogService.getById(id);
            String sysyUrl = blog.getSysyUrl();
            String md = null;
            if (null != sysyUrl) {
                md = resourceService.dowloadTextFile(sysyUrl);
            }
            return md;
        } catch (Exception e) {
            LOG.error("文本下载失败，发生异常", e);
        }
        return null;
    }

    private Resource doResource(Integer id, String fileName) {
        Resource r = new Resource();
        r.setBlogid(id);
        r.setFilename(fileName);
        r.setUrl("/");
        return r;
    }

    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.DESC, "createtime");
    }

    //------------------
    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
        super.setBaseService(blogService);
    }

}








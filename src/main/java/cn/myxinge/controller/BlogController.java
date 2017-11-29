package cn.myxinge.controller;

import cn.myxinge.common.WriteMyLog;
import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.BlogService;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenxinghua on 2017/11/9.
 * <p>
 * 博客管理控制层
 */
@RestController
@RequestMapping("/admin/blog")
public class BlogController{
    private static final Logger LOG = LoggerFactory.getLogger(BlogController.class);
    @Autowired
    private BlogService blogService;
    @Autowired
    private ResourceService resourceService;

    /**
     * 根据博客url获取博客内容
     *
     * @param url
     * @param model
     * @return
     */
    @RequestMapping(value = "/{url}",method = {RequestMethod.GET})
    public Blog showBlog(@PathVariable String url, Model model) {
        Blog blog = blogService.getBlog(url);
        return blog;
    }

    /**
     * 最新博客 6 篇
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/newerBlog",method = {RequestMethod.GET})
    public List<Blog> newerBlog(Model model) {
        Page<Blog> newerBlog = blogService.newerBlog(6);
        return newerBlog.getContent();
    }

    /**
     * 添加博客
     * html页面 ， blog 博客对象 ，用来存储到数据库
     */
    @RequestMapping(value = "/add",method = {RequestMethod.POST})
    public JSONObject addBlog(MultipartFile html, Blog blog) {

        if (html == null || StringUtils.isEmpty(html.getOriginalFilename())) {
            return ResponseUtil.returnJson(true, "博客页面为空");
        }

        blogService.addBlog(html, blog);

        return ResponseUtil.returnJson(true, "成功");
    }

    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    public Map list(Integer page, Integer rows) {

        Page<Blog> data = blogService.list(null, null, page, rows);
        long total = blogService.getCount(null);

        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());

        return mapData;
    }

    @RequestMapping(value = "/listWithoutState",method = {RequestMethod.GET,RequestMethod.POST})
    public Map listWithoutState(Integer page, Integer rows) {

        Page<Blog> data = blogService.listWithoutState(null, null, page, rows);
        long total = blogService.getCount(null);
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());

        return mapData;
    }


    //跟据id删除 -- 带资源
    @RequestMapping(value = "/delete",method = {RequestMethod.POST})
    public JSONObject delete(Integer id) throws Exception {
        if (null == id) {
            return ResponseUtil.returnJson(false, "ID为空");
        }

        Blog blogById = blogService.getBlogById(id);
        Resource resource = new Resource();
        resource.setBlogid(id);
        String result = resourceService.deleteWithBlog(resource);

        if ("success".equals(result)) {

            blogService.delete(id);
            return ResponseUtil.returnJson(true, "成功");
        }

        return ResponseUtil.returnJson(false, result);
    }

    @RequestMapping(value = "/findById/{id}",method = {RequestMethod.GET})
    public Blog findById(@PathVariable Integer id) {
        if (null == id) {
            return null;
        }

        return blogService.getBlogById(id);
    }

    @RequestMapping(value = "/changState/{id}",method = {RequestMethod.PUT})
    public JSONObject changState(@PathVariable Integer id) {
        if (null != id) {
            Blog blogById = blogService.getBlogById(id);
            Integer state = blogById.getState();

            if (Blog.STATE_OFFLINE == state.intValue()) {
                blogById.setState(Blog.STATE_ONLINE);
            } else if (Blog.STATE_ONLINE == state.intValue()) {
                blogById.setState(Blog.STATE_OFFLINE);
            } else {
                return ResponseUtil.returnJson(false, "失败,状态未知");
            }

            blogService.save(blogById);
            return ResponseUtil.returnJson(true, "成功");
        }

        return ResponseUtil.returnJson(false, "失败,博客不存在");
    }

    @RequestMapping(value = "/update",method = {RequestMethod.POST})
    public JSONObject update(Blog blog, MultipartFile html) throws Exception {
        if (null == blog || null == blog.getId()) {
            return ResponseUtil.returnJson(false, "失败,博客不存在");
        }

        Blog blogById = blogService.getBlogById(blog.getId());

        //页面为空
        if (html == null || StringUtils.isEmpty(html.getOriginalFilename())) {
            //保存即可
            blogById.setSubject(blog.getSubject());
            blogById.setTitle(blog.getTitle());
            blogById.setAuth(blog.getAuth());
            blogById.setUpdatetime(new Date());
            blogById.setUrl(blog.getUrl());
            blogService.save(blogById);

        } else {

            if(!html.getOriginalFilename().endsWith(".html")){
                return ResponseUtil.returnJson(false, "文件不是html");
            }

            blog.setCreatetime(blogById.getCreatetime());
            blog.setUpdatetime(new Date());
            blog.setUrl(blogById.getUrl());
            //添加
            int i = blogService.addBlog(html, blog);

            if(0 != i){
                return ResponseUtil.returnJson(false, "修改保存失败");
            }

            //删除原来的页面
            Resource resource = new Resource();
            resource.setBlogid(blogById.getId());
            String rtn = resourceService.deleteHtml(resource);

            if (!"success".equals(rtn)) {
                return ResponseUtil.returnJson(false, "原有Html页面删除失败。");
            }
        }

        return ResponseUtil.returnJson(true, "成功");
    }

}








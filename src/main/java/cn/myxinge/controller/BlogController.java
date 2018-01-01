package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.BlogService;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.BASE64DecUtil;
import cn.myxinge.utils.FileUtil;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.rmi.runtime.Log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

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
        long total = blogService.getCount(ex);
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
    public JSONObject showBlog(@PathVariable String url) {
        Blog blog = blogService.getBlogByUrl(url);
        //顺带把上一篇和下一篇带出去
        //todo 上一篇下一篇取消
//        Map<String, Blog> map = blogService.findPreAndNext(blog);
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("curBlog", blog);
//        jsonMap.put("preAndNext", map);
        return new JSONObject(jsonMap);
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
    public JSONObject changState(@PathVariable Integer id) throws Exception {
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
    public JSONObject saveWithoutHtml(Blog blog) {

        LOG.info("保存博客开始：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        if (null != blog.getId()) {
            //此时标识数据更新，而不是添加
            Blog syBlog = blogService.getById(blog.getId());
            blog.setCreatetime(syBlog.getCreatetime());
            blog.setSysyUrl(syBlog.getSysyUrl());
            blog.setMainImgUrl(syBlog.getMainImgUrl());
        }
        blog.setState(Blog.STATE_OFFLINE);
        blog.setAuth("Xingchen");
        String rtn = super.add(blog);   //注：虽然方法名叫做add,但当数据有id时，表明是更新，执行的是update
        if ("1".equals(rtn)) {
            JSONObject json = ResponseUtil.returnJson(true, "保存成功");
            Object eval = JSONPath.eval(json, "$.success");
            JSONPath.set(json, "$.id", blog.getId());
            return json;
        } else {
            return ResponseUtil.returnJson(false, "存储失败");
        }
    }

    @RequestMapping("/uploadImg")
    public JSONObject uploadImg(String imgData, Integer id) throws Exception {
        if (null == id) {
            return ResponseUtil.returnJson(false, "ID为空");
        }
        if (StringUtils.isEmpty(imgData)) {
            return ResponseUtil.returnJson(false, "图片信息为空");
        }

        imgData = imgData.replace("data:image/jpeg;base64,", "");

        Blog byId = blogService.getById(id);
        if (null == byId) {
            return ResponseUtil.returnJson(false, "博客为空");
        } else {
            if (null != byId.getMainImgUrl()) {
                //删除原有的
                Resource resource = new Resource();
                resource.setSysyUrl(byId.getMainImgUrl());
                resourceService.deleteSysFile(resource);
            }
        }

        String rootPath = this.getClass().getResource("/").getPath();
        File file = new File(rootPath + "static/temp/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = FileUtil.uuidName(".jpg");
        try {
            boolean b = BASE64DecUtil.generateImage(imgData, rootPath + "static/temp/" + fileName);
            if (b) {
                String upload = resourceService.upload(rootPath + "static/temp/" + fileName, "jpg");
                Resource resource = new Resource();

                //存储资源表
                resource.setSuffix("jpg");
                resource.setDescription(byId.getTitle() + " - 封面图片文件");
                resource.setSysyUrl(upload);
                resource.setState(Resource.STATE_USE);
                resource.setCreatetime(new Date());
                resource.setUrl("/");
                resource.setBlogid(id);
                resource.setFilename(fileName);
                resourceService.add(resource);

                if (!"-1".equals(upload)) {
                    byId.setMainImgUrl(upload);
                    blogService.update(byId);
                    return ResponseUtil.returnJson(true, "上传成功");
                }
            } else {
                return ResponseUtil.returnJson(false, "图片解析失败");
            }
        } catch (Exception e) {
            LOG.error("上传失败，发生异常", e);
        } finally {
            //删除临时文件
            File _file = new File(rootPath + "static/temp/" + fileName);
            if (file != null) {
                file.delete();
            }
        }
        return ResponseUtil.returnJson(false, "上传失败，发生异常");
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
            return ResponseUtil.returnJson(true, "上传成功");
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

    //归档实现：当前年份，月份 - 2017 年之间的所有博客信息，使用Map层次封装
    @RequestMapping("/listByArchives")
    public List listByArchives() {
        return blogService.listByArchives();
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








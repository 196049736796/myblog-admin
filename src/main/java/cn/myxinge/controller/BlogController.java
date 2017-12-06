package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.BaseService;
import cn.myxinge.service.BlogService;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.FileUtil;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
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
    @Value("${baseUrl}")
    private String baseUrl;


    @Override
    @RequestMapping("/list")
    public Map list(Blog t, Integer page, Integer rows) {
        return super.list(t, page, rows);
    }

    @RequestMapping(value = "/listOnLine", method = {RequestMethod.GET, RequestMethod.POST})
    public Map listWithoutState(Blog t,Integer page, Integer rows) {
        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("state",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");
        Blog blog = new Blog();
        blog.setState(Blog.STATE_ONLINE);
        Example<Blog> ex = Example.of(blog, ma);
        Page<Blog> data = blogService.listOnWhere(t,page,rows,getSort(),ex);
        long total = blogService.getCount(null);
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());
        return mapData;
    }

    //------------------
    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
        super.setBaseService(blogService);
    }

    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.DESC, "createtime");
    }

    /**
     * 根据博客url获取博客内容
     *
     * @param url
     * @param model
     * @return
     */
//    @RequestMapping(value = "/{url}", method = {RequestMethod.GET})
//    public Blog showBlog(@PathVariable String url, Model model) {
//        Blog blog = blogService.getBlog(url);
//        return blog;
//    }
//
//    /**
//     * 最新博客 6 篇
//     *
//     * @param model
//     * @return
//     */
//    @RequestMapping(value = "/newerBlog", method = {RequestMethod.GET})
//    public List<Blog> newerBlog(Model model) {
//        Page<Blog> newerBlog = blogService.newerBlog(6);
//        return newerBlog.getContent();
//    }
//
//    /**
//     * 添加博客
//     * html页面 ， blog 博客对象 ，用来存储到数据库
//     */
//    @RequestMapping(value = "/add", method = {RequestMethod.POST})
//    public JSONObject addBlog(MultipartFile html, MultipartFile mainImg, Blog blog) throws IOException {
//
//        if (html == null || StringUtils.isEmpty(html.getOriginalFilename()) ||
//                !html.getOriginalFilename().endsWith(".html")) {
//            return ResponseUtil.returnJson(true, "博客页面为空或文件 格式有误");
//        }
//
//        if (mainImg == null || StringUtils.isEmpty(mainImg.getOriginalFilename()) ||
//                !FileUtil.isPicture(mainImg.getOriginalFilename())) {
//            return ResponseUtil.returnJson(true, "需要一个大图");
//        }
//        blogService.addBlog(blog);
//        //解析html，将图片上传至FastDFS,并更改其URL
//        byte[] htmlBytes = html.getBytes();
//        String htmlStr = new String(htmlBytes, "utf-8");
//        //找到<img/>的正则表达式
//        Set<String> imageSrc = getImageSrc(htmlStr);
//        //存储资源图片
//        Resource r = null;
//        FileInputStream is = null;
//        for (String img : imageSrc) {
//            String fileName = img.substring(img.lastIndexOf("\\") + 1);
//            r = getResoByName(FileUtil.uuidName(fileName), blog);
//            is = new FileInputStream(img);
//            //存储资源
//            String sysUrl = resourceService.upload(is, r);
//            if ("-1".equals(sysUrl)) {
//                LOG.error("资源：" + r.getFilename() + "上传失败");
//                continue;
//            }
//            //更新路径
//            htmlStr = htmlStr.replace(img, baseUrl + sysUrl);
//        }
//
//        //存储大图
//        String fileName = mainImg.getOriginalFilename()
//                .substring(mainImg.getOriginalFilename().lastIndexOf("/") + 1);
//        r = getResoByName(FileUtil.uuidName(fileName), blog);
//        String mainImgUrl = resourceService.upload(mainImg.getInputStream(), r);
//        if ("-1".equals(mainImgUrl)) {
//            LOG.error("HTML上传失败");
//            //此时，因为资源已经上传，就算html上传失败也要存储博客
//        }
//
//        //存储html
//        //写文件
//        String dirPath = this.getClass().getResource("/")
//                .getPath() + "static/temp/";
//        File f = new File(dirPath);
//        if (!f.exists()) {
//            f.mkdirs();
//        }
//
//        fileName = html.getOriginalFilename()
//                .substring(html.getOriginalFilename().lastIndexOf("/") + 1);
//        r = getResoByName(fileName, blog);
//        String htmlUrl = resourceService.upload(new ByteArrayInputStream(htmlStr.getBytes()), r);
//
//        if ("-1".equals(htmlUrl)) {
//            LOG.error("主图上传失败");
//            //此时，因为资源已经上传，就算上传失败也要存储博客
//        }
//        blog.setSysyUrl(htmlUrl);
//        blog.setMainImgUrl(mainImgUrl);
//        blogService.save(blog);
//        return ResponseUtil.returnJson(true, "成功");
//    }
//
//    private Resource getResoByName(String fileName, Blog blog) {
//        Resource r = new Resource();
//        r.setUrl(fileName);
//        r.setFilename(fileName);
//        r.setSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));
//        r.setBlogid(blog.getId());
//        return r;
//    }
//
//    @RequestMapping(value = "/list", method = {RequestMethod.GET})
//    public Map list(Integer page, Integer rows) {
//
//        Page<Blog> data = blogService.list(null, page, rows);
//        long total = blogService.getCount(null);
//
//        Map<String, Object> mapData = new HashMap<String, Object>();
//        mapData.put("total", total);
//        mapData.put("rows", data.getContent());
//
//        return mapData;
//    }
//
//    @RequestMapping(value = "/listWithoutState", method = {RequestMethod.GET, RequestMethod.POST})
//    public Map listWithoutState(Integer page, Integer rows) {
//
//        Page<Blog> data = blogService.listWithoutState(null, null, page, rows);
//        long total = blogService.getCount(null);
//        Map<String, Object> mapData = new HashMap<String, Object>();
//        mapData.put("total", total);
//        mapData.put("rows", data.getContent());
//
//        return mapData;
//    }
//
//
//    //跟据id删除 -- 带资源
//    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
//    public JSONObject delete(Integer id) throws Exception {
//        if (null == id) {
//            return ResponseUtil.returnJson(false, "ID为空");
//        }
//
//        Blog blogById = blogService.getBlogById(id);
//        Resource resource = new Resource();
//        resource.setBlogid(id);
//        String result = resourceService.deleteWithBlog(resource);
//
//        if ("success".equals(result)) {
//
//            blogService.delete(id);
//            return ResponseUtil.returnJson(true, "成功");
//        }
//
//        return ResponseUtil.returnJson(false, result);
//    }
//
//    @RequestMapping(value = "/findById/{id}", method = {RequestMethod.GET})
//    public Blog findById(@PathVariable Integer id) {
//        if (null == id) {
//            return null;
//        }
//
//        return blogService.getBlogById(id);
//    }
//
//    @RequestMapping(value = "/changState/{id}", method = {RequestMethod.PUT})
//    public JSONObject changState(@PathVariable Integer id) {
//        if (null != id) {
//            Blog blogById = blogService.getBlogById(id);
//            Integer state = blogById.getState();
//
//            if (Blog.STATE_OFFLINE == state.intValue()) {
//                blogById.setState(Blog.STATE_ONLINE);
//            } else if (Blog.STATE_ONLINE == state.intValue()) {
//                blogById.setState(Blog.STATE_OFFLINE);
//            } else {
//                return ResponseUtil.returnJson(false, "失败,状态未知");
//            }
//
//            blogService.save(blogById);
//            return ResponseUtil.returnJson(true, "成功");
//        }
//
//        return ResponseUtil.returnJson(false, "失败,博客不存在");
//    }
//
//    @RequestMapping(value = "/update", method = {RequestMethod.POST})
//    public JSONObject update(Blog blog, MultipartFile html) throws Exception {
//        if (null == blog || null == blog.getId()) {
//            return ResponseUtil.returnJson(false, "失败,博客不存在");
//        }
//
//        Blog blogById = blogService.getBlogById(blog.getId());
//
//        //页面为空
//        if (html == null || StringUtils.isEmpty(html.getOriginalFilename())) {
//            //保存即可
//            blogById.setSubject(blog.getSubject());
//            blogById.setTitle(blog.getTitle());
//            blogById.setAuth(blog.getAuth());
//            blogById.setUpdatetime(new Date());
//            blogById.setUrl(blog.getUrl());
//            blogService.save(blogById);
//
//        } else {
//            //todo
//            /*if (!html.getOriginalFilename().endsWith(".html")) {
//                return ResponseUtil.returnJson(false, "文件不是html");
//            }
//
//            blog.setCreatetime(blogById.getCreatetime());
//            blog.setUpdatetime(new Date());
//            blog.setUrl(blogById.getUrl());
//            //添加
//            String htmlUrl = resourceService.upload(html, blog, null);
//            if ("-1".equals(htmlUrl)) {
//                return ResponseUtil.returnJson(false, "文件上传失败");
//            }
//            blog.setSysyUrl(htmlUrl);
//            int i = blogService.addBlog(blog);
//
//            if (0 != i) {
//                return ResponseUtil.returnJson(false, "修改保存失败");
//            }
//
//            //删除原来的页面
//            Resource resource = new Resource();
//            resource.setBlogid(blogById.getId());
//            String rtn = resourceService.deleteHtml(resource);
//
//            if (!"success".equals(rtn)) {
//                return ResponseUtil.returnJson(false, "原有Html页面删除失败。");
//            }*/
//        }
//
//        return ResponseUtil.returnJson(true, "成功");
//    }
//
//    /**
//     * 取出所有img标签，将其上传至服务器
//     *
//     * @param htmlCode
//     * @return
//     */
//    public static Set getImageSrc(String htmlCode) {
//        Set<String> imageSrcList = new HashSet<String>();
//        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
//        Matcher m = p.matcher(htmlCode);
//        String quote = null;
//        String src = null;
//        while (m.find()) {
//            quote = m.group(1);
//
//            // src=https://sms.reyo.cn:443/temp/screenshot/zY9Ur-KcyY6-2fVB1-1FSH4.png
//            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
//            imageSrcList.add(src);
//        }
//        return imageSrcList;
//    }
}








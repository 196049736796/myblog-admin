package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.BlogService;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxinghua on 2017/12/2.
 */
@RestController
@RequestMapping("/admin/resouce")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    @Value("${baseUrl}")
    private String baseUrl;

    //    @RequestMapping("/uploadRe")
    public JSONObject uploadRe(MultipartFile image_file, MultipartFile resource, Integer blogId, String url) throws IOException {
        if (StringUtils.isEmpty(url) || null == resource) {
            return ResponseUtil.returnJson(false, "资源为空或路径未填写");
        }
        Resource r = new Resource();
        r.setBlogid(blogId);
        String sysurl = resourceService.upload(resource.getInputStream(), r);
        if ("-1".equals(sysurl)) {
            return ResponseUtil.returnJson(false, "上传失败");
        } else {
            return ResponseUtil.returnJson(true, "上传成功");
        }
    }

    @RequestMapping(value = "/listWithoutState", method = {RequestMethod.GET, RequestMethod.POST})
    public Map listWithoutState(Integer page, Integer rows) {

        Page<Resource> data = resourceService.list(page, rows);
        long total = resourceService.getCount(null);
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());
        return mapData;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public JSONObject delete(Integer id) throws Exception {
        if (null == id) {
            return ResponseUtil.returnJson(false, "ID为空");
        }

        Resource r = resourceService.getById(id);
        String rtn = resourceService.deleteSysFile(r);
        if ("-1".equals(rtn)) {
            return ResponseUtil.returnJson(false, "从文件管理系统删除文件失败");
        }
        r.setState(Resource.STATE_UNUSE);
        resourceService.save(r);
        return ResponseUtil.returnJson(true, "已从文件管理系统删除文件信息");
    }

    @RequestMapping("/uploadRe")
    public JSONObject uploadRe2(MultipartFile image_file) throws IOException {

        Resource resource = new Resource();
        resource.setBlogid(-1);
        resource.setUrl("-");
        resource.setFilename(image_file.getOriginalFilename());

        String sysurl = resourceService.upload(image_file.getInputStream(), resource);
        Map rtn = new HashMap();
        if ("-1".equals(sysurl)) {
            rtn.put("success", -1);
            rtn.put("url", null);
            rtn.put("message", "上传失败");
            return new JSONObject(rtn);
        } else {
            rtn.put("success", 1);
            rtn.put("url", baseUrl.concat(sysurl));
            rtn.put("message", "success");
            return new JSONObject(rtn);
        }
    }

    @RequestMapping("/uploadHtml")
    public String uploadHtml(String md,String title){
        Resource r = new Resource();
        r.setFilename(title.concat(".md"));
        r.setUrl(r.getFilename());
        r.setBlogid(99);
        String mdUrl = resourceService.upload(new ByteArrayInputStream(md.getBytes()), r);
        return mdUrl;
    }
}

package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.BlogService;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by chenxinghua on 2017/12/2.
 */
@RestController
@RequestMapping("/admin/resouce")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    @RequestMapping("/uploadRe")
    public JSONObject uploadRe(MultipartFile resource,Integer blogId,String url) throws IOException {
        if(StringUtils.isEmpty(url) || null == resource){
            return ResponseUtil.returnJson(false, "资源为空或路径未填写");
        }
        Resource r = new Resource();
        r.setBlogid(blogId);
        String sysurl = resourceService.upload(resource.getInputStream(),r);
        if("-1".equals(sysurl)){
            return ResponseUtil.returnJson(false, "上传失败");
        }else{
            return ResponseUtil.returnJson(true, "上传成功");
        }
    }
}

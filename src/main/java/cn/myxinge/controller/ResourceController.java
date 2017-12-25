package cn.myxinge.controller;

import cn.myxinge.entity.Resource;
import cn.myxinge.entity.User;
import cn.myxinge.service.AuthService;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.BASE64DecUtil;
import cn.myxinge.utils.FileUtil;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxinghua on 2017/12/2.
 */
@RestController
@RequestMapping("/admin/resouce")
public class ResourceController extends BaseController<Resource> {
    private Logger LOG = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private ResourceService resourceService;
    @Value("${baseUrl}")
    private String baseUrl;

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Map listWithoutState(Resource resource, Integer page, Integer rows) {

        return super.list(resource, page, rows);
    }

    @RequestMapping("/uploadRe")
    public JSONObject uploadRe2(Integer blogId, MultipartFile image_file) throws IOException {

        Map rtn = new HashMap();
        if (blogId == null) {
            rtn.put("success", -1);
            rtn.put("url", null);
            rtn.put("message", "博客未知");
            return new JSONObject(rtn);
        }

        Resource resource = new Resource();
        resource.setBlogid(blogId);
        resource.setUrl("/");
        resource.setFilename(image_file.getOriginalFilename());

        String sysurl = resourceService.upload(image_file.getInputStream(), resource);
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

    @RequestMapping("/delete")
    public JSONObject delete(Integer id) {
        Resource r = resourceService.getById(id);
        try {
            String rtn = resourceService.deleteSysFile(r);
            if ("success".equals(rtn)) {
                return ResponseUtil.returnJson(true, "删除成功");
            }
        } catch (Exception e) {
            LOG.error("删除失败，发现异常", e);
            return ResponseUtil.returnJson(false, "删除失败,发现异常");
        }
        return ResponseUtil.returnJson(false, "删除失败");
    }

    /**
     * 用户头像上传，返回路径
     */
    @RequestMapping("/uploadUserAvatar")
    @ResponseBody
    public String uploadUserAvatar(Integer userId, String image) throws Exception {
        if (StringUtils.isEmpty(image) || null == userId) {
            return null;
        }

        //更新数据
        User user = authService.getById(userId);
        //删除掉以前的
        if (!StringUtils.isEmpty(user.getAvatar_url())) {
            Resource resource = new Resource();
            resource.setSysyUrl(user.getAvatar_url());
            resourceService.deleteSysFile(resource);
            LOG.info("头像上传中，正在删除原来的图片： " + user.getAvatar_url());
        }

        //上传开始
        image = image.replace("data:image/png;base64,", "");

        String rootPath = this.getClass().getResource("/").getPath();
        File file = new File(rootPath + "static/temp/");
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = FileUtil.uuidName(".png");
        try {
            boolean b = BASE64DecUtil.generateImage(image, rootPath + "static/temp/" + fileName);
            if (b) {
                String upload = resourceService.upload(rootPath + "static/temp/" + fileName, "png");

                user.setAvatar_url("http://www.myxinge.cn/" + upload);
                authService.update(user);

                return "http://www.myxinge.cn/" + upload;
            }
        } catch (Exception e) {
            LOG.error("头像上传失败，发生异常", e);
        }

        return "/images/img.jpg";
    }

    @Autowired
    public void setBlogService(ResourceService resourceService) {
        this.resourceService = resourceService;
        super.setBaseService(resourceService);
    }

    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.DESC, "createtime");
    }
}

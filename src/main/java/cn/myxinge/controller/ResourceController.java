package cn.myxinge.controller;

import cn.myxinge.entity.Resource;
import cn.myxinge.service.MenuService;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxinghua on 2017/12/2.
 */
@RestController
@RequestMapping("/admin/resouce")
public class ResourceController extends BaseController<Resource>{
    private Logger LOG = LoggerFactory.getLogger(ResourceController.class);
    @Autowired
    private ResourceService resourceService;
    @Value("${baseUrl}")
    private String baseUrl;

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Map listWithoutState(Resource resource,Integer page, Integer rows) {

        return super.list(resource,page, rows);
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

    @RequestMapping("/delete")
    public JSONObject delete(Integer id) {
        Resource r = resourceService.getById(id);
        try {
            String rtn = resourceService.deleteSysFile(r);
            if("success".equals(rtn)){
               return ResponseUtil.returnJson(true,"删除成功");
            }
        } catch (Exception e) {
           LOG.error("删除失败，发现异常",e);
            return ResponseUtil.returnJson(false,"删除失败,发现异常");
        }
        return ResponseUtil.returnJson(false,"删除失败");
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

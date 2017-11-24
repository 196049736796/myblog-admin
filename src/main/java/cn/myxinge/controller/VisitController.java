package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.BoardMsg;
import cn.myxinge.entity.Resource;
import cn.myxinge.entity.VisitIp;
import cn.myxinge.service.VisitIpService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenxinghua on 2017/11/23.
 */
@RequestMapping(value = "/admin/visit")
@RestController
public class VisitController {
    private Logger LOG = LoggerFactory.getLogger(VisitController.class);
    @Autowired
    private VisitIpService visitIpService;

    @RequestMapping(value = "/save",method = {RequestMethod.POST})
    public JSONObject save(VisitIp visitIp){

        visitIpService.save(visitIp);
        return ResponseUtil.returnJson(true,"success");
    }

    @RequestMapping(value = "/listWithoutState",method = {RequestMethod.GET,RequestMethod.POST})
    public Map list(Integer page, Integer rows){
        Page<VisitIp> data = visitIpService.list(page, rows);
        long total = visitIpService.getCount(null);

        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());

        return mapData;
    }

    //跟据id删除 -- 带资源
    @RequestMapping(value = "/delete",method = {RequestMethod.DELETE})
    public JSONObject delete(Integer id) throws Exception {
        if (null == id) {
            return ResponseUtil.returnJson(false, "ID为空");
        }

        try {
            visitIpService.delete(id);
        } catch (Exception e) {
            LOG.error("删除失败",e);
            return ResponseUtil.returnJson(false, "出现异常");
        }

        return ResponseUtil.returnJson(true, "成功");
    }
}

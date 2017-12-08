package cn.myxinge.controller;

import cn.myxinge.entity.VisitIp;
import cn.myxinge.service.MenuService;
import cn.myxinge.service.VisitIpService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * Created by chenxinghua on 2017/11/23.
 */
@RequestMapping(value = "/admin/visit")
@RestController
public class VisitController extends BaseController<VisitIp> {
    private Logger LOG = LoggerFactory.getLogger(VisitController.class);
    @Autowired
    private VisitIpService visitIpService;

    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public JSONObject save(VisitIp visitIp) throws ParseException {
        VisitIp vi = visitIpService.findByIP(visitIp);
        if (null != vi) {
            vi.setVisittime(new Date());
            vi.setVisitNum(vi.getVisitNum() == null ? 1 : vi.getVisitNum() + 1);
            super.update(vi);
            return ResponseUtil.returnJson(true, "success");
        }
        visitIp.setVisitNum(1L);
        String rtb = super.add(visitIp);
        if ("1".equals(rtb)) {
            return ResponseUtil.returnJson(true, "success");
        }
        return ResponseUtil.returnJson(false, "failre");
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Map list(VisitIp visitIp, Integer page, Integer rows) {
        return super.list(visitIp, page, rows);
    }

    //跟据id删除 -- 带资源
    @RequestMapping(value = "/delete", method = {RequestMethod.DELETE})
    public JSONObject delete(Integer id) throws Exception {
        if (null == id) {
            return ResponseUtil.returnJson(false, "ID为空");
        }

        try {
            super.delete(id);
        } catch (Exception e) {
            LOG.error("删除失败", e);
            return ResponseUtil.returnJson(false, "出现异常");
        }

        return ResponseUtil.returnJson(true, "成功");
    }

    @Autowired
    public void setBlogService(VisitIpService visitIpService) {
        this.visitIpService = visitIpService;
        super.setBaseService(visitIpService);
    }

    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.DESC, "visittime");
    }

}

package cn.myxinge.controller;

import cn.myxinge.entity.Constants;
import cn.myxinge.service.BaseService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XingChen on 201--7/11/19.
 * <p>
 * controller 父类
 */
public class BaseController<T> {
    private Logger LOG = LoggerFactory.getLogger(BaseController.class);
    private BaseService baseService;
    private Sort sort;


    /**
     * 增
     */
    public String add(T t) {
        try {
            baseService.add(t);
        } catch (Exception e) {
            LOG.error(t + "：存储失败", e);
            return Constants.STATE_FAILRE;
        }
        return Constants.STATE_SUCCESS;
    }


    /**删*/
    public String delete(Serializable id) {
        try {
            baseService.delete(id);
        } catch (Exception e) {
            LOG.error(id + "：删除失败", e);
            return Constants.STATE_FAILRE;
        }
        return Constants.STATE_SUCCESS;
    }



    /**改*/
    public String update(T t){
        try {
            baseService.update(t);
        } catch (Exception e) {
            LOG.error(t + "：修改失败", e);
            return Constants.STATE_FAILRE;
        }
        return Constants.STATE_SUCCESS;
    }



    /**返回整个列表（加排序条件，如无，传null）*/
    public Map list(T t, Integer page, Integer rows) {
        Page<T> data = baseService.list(null, page, rows,getSort());
        long total = baseService.getCount(null);
        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());
        return mapData;
    }

    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

    public Sort getSort() {
        return sort;
    }
}









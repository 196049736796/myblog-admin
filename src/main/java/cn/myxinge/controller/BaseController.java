package cn.myxinge.controller;

import cn.myxinge.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XingChen on 201--7/11/19.
 *
 * 父类
 */
//@RestController
//@RequestMapping("/page")
public class BaseController<T> {

//    @Autowired
    private BaseService baseService;

    @RequestMapping("/{t}/list")
    public Map list(@PathVariable String t,Integer page,Integer rows){

//        int firstResult = (page - 1) * rows;
        Page<T> data = baseService.list(null, null, page, rows);
        long total = baseService.getCount(null);

        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());

        return mapData;
    }
}









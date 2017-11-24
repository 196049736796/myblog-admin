package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.BoardMsg;
import cn.myxinge.service.BoardMsgService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XingChen on 2017/11/7.
 * 处理留言板信息存储
 */
@RestController
@RequestMapping("/admin/boardMsg")
public class BoardMsgController {

    @Autowired
    private BoardMsgService boardMsgService;

    @RequestMapping(value = "/sendMessage",method = {RequestMethod.POST})
    public JSONObject saveMsg(BoardMsg msg) {
        if (null == msg) {
            return ResponseUtil.returnJson(true,"msg required!");
        }
        //存储
        msg.setCreatetime(new Date());
        boardMsgService.save(msg);
        return ResponseUtil.returnJson(true,"success");
    }

    @RequestMapping(value = "/listWithoutState",method = {RequestMethod.GET,RequestMethod.POST})
    public Map list(Integer page, Integer rows){
        Page<BoardMsg> data = boardMsgService.list(page, rows);
        long total = boardMsgService.getCount(null);

        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("total", total);
        mapData.put("rows", data.getContent());

        return mapData;
    }
}

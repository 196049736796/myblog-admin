package cn.myxinge.controller;

import cn.myxinge.entity.BoardMsg;
import cn.myxinge.entity.Comments;
import cn.myxinge.service.BlogService;
import cn.myxinge.service.BoardMsgService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by XingChen on 2017/11/7.
 * 处理留言板信息存储
 */
@RestController
@RequestMapping("/admin/boardMsg")
public class BoardMsgController extends BaseController<BoardMsg> {

    @Autowired
    private BoardMsgService boardMsgService;

    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public JSONObject saveMsg(BoardMsg msg) {
        if (null == msg) {
            return ResponseUtil.returnJson(true, "msg required!");
        }
        //存储
        msg.setCreatetime(new Date());
        try {
            boardMsgService.add(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.returnJson(false, "system error");
        }
        return ResponseUtil.returnJson(true, "success");
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Map list(BoardMsg boardMsg, Integer page, Integer rows) {
        return super.list(boardMsg, page, rows);
    }

    //无分页，直接返回
    @RequestMapping("/all")
    public List all() {
        return boardMsgService.all(null, getSort());
    }

    @RequestMapping("/del/{id}")
    public String delete(@PathVariable Integer id) {
        return super.delete(id);
    }

    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.DESC, "createtime");
    }

    @Autowired
    public void setBlogService(BoardMsgService boardMsgService) {
        this.boardMsgService = boardMsgService;
        super.setBaseService(boardMsgService);
    }
}

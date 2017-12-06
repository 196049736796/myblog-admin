package cn.myxinge.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenxinghua on 2017/11/28.
 * 评论处理
 */
@RestController
public class CommentController extends BaseController{
    @RequestMapping("/saveComment")
    public void test(@RequestBody String body){

    }
}












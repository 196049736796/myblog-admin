package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by XingChen on 2017/11/20--.
 */
@Controller
public class PageController {

    @RequestMapping(value = "/",method = {RequestMethod.GET})
    public String allBlog() {

        return "redirect:/page/login.html";
    }

   /* @RequestMapping("/error")
    public String toError() {

        return "redirect:/page/error.html";
    }*/
}

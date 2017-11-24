package cn.myxinge.controller;

import cn.myxinge.utils.ResponseUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by XingChen on 2017/11/19.
 */
@RestController
@RequestMapping("/page")
public class UserController {

    @RequestMapping(value = "/user_getName",method = {RequestMethod.GET})
    public String getName(){
        return "Xingchen";
    }

    @RequestMapping("/user_isLogin")
    public String isLogin(HttpServletRequest request){
        Object login = request.getSession().getAttribute("login");
        if(login != null){
            return ResponseUtil.returnMsg(true,"已登录");
        }

        return  ResponseUtil.returnMsg(false,"未登录");
//        return ResponseUtil.returnMsg(true,"已登录");
    }

    @RequestMapping(value = "/user_login",method = {RequestMethod.POST})
    public String user_login(String username,String password,HttpServletRequest request){
        if("cxh114".equals(username) && "rootcxh114".equals(password)){
            request.getSession().setAttribute("login","loginSuccess");
            return ResponseUtil.returnMsg(true,"成功");
        }

        return ResponseUtil.returnMsg(false,"失败");
    }

    @RequestMapping(value = "/user_quit",method = {RequestMethod.POST})
    public void user_quit(HttpServletRequest request){
        request.getSession().setAttribute("login",null);
    }
}













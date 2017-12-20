package cn.myxinge.controller;

import cn.myxinge.entity.User;
import cn.myxinge.service.AuthService;
import cn.myxinge.utils.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.JSONPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XingChen on 2017/12/20.
 */
@Controller
@RequestMapping("/admin/user")
public class AuthController extends BaseController<User> {

    @Autowired
    private AuthService authService;

    @RequestMapping("/add")
    @ResponseBody
    public String add(User user) {
        user.setState(User.STATE_UNACTIVATED);
        user.setCreated_at(new Date());
        return super.add(user);
    }

    @RequestMapping("/log")
    @ResponseBody
    public String log(User user) {
        User login = authService.log(user);
        String msg = "用户名或密码错误，登录失败。";
        Map<String, Object> map = new HashMap<>();
        if (null != login) {
            msg = "success";
            if (User.STATE_UNACTIVATED.equals(login.getState())) {
                msg = "请先前往邮箱激活账号";
            }
            if (User.STATE_INVAITED.equals(login.getState())) {
                msg = "账号不可用，如有疑问请咨询。 email: cxh1960497367@163.com";
            }
            map.put("success", msg);
            String jsonString = JSONObject.toJSONString(login);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            jsonObject.remove("pwd");
            map.put("userInfo", jsonObject.toJSONString());
        } else {
            map.put("success", msg);
        }
        JSONObject json = new JSONObject(map);
        return json.toJSONString();
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(User user) {
        user.setUpdated_at(new Date());
        return super.update(user);
    }

    @Autowired
    public void setBlogService(AuthService authService) {
        this.authService = authService;
        super.setBaseService(authService);
    }

    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.DESC, "created_at");
    }
}
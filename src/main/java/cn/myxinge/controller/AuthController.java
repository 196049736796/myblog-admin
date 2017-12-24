package cn.myxinge.controller;

import cn.myxinge.entity.Constants;
import cn.myxinge.entity.User;
import cn.myxinge.service.AuthService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by XingChen on 2017/12/20.
 */
@Controller
@RequestMapping("/admin/user")
public class AuthController extends BaseController<User> {

    private Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${db.mailU}")
    private String mailU;

    @Value("${confirmUrl}")
    private String confirmUrl;

    @Autowired
    private AuthService authService;

    @RequestMapping("/add")
    @ResponseBody
    public String add(User user) {
        User byEmail = authService.getByEmail(user);
        if (byEmail != null) {
            return "该邮箱已经注册过，请直接在登录页面登录,如果忘记密码，可以选择找回密码";
        }

        //默认头像
        user.setConfirm_id(UUID.randomUUID().toString());
        user.setAvatar_url("/images/img.jpg");
        user.setState(User.STATE_UNACTIVATED);
        user.setCreated_at(new Date());

        String rtn = super.add(user);
        //todo 发送邮件
        if (Constants.STATE_SUCCESS.equals(rtn)) {
            return sendMail(user);
        }
        return Constants.STATE_FAILRE;
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
//            if (User.STATE_INVAITED.equals(login.getState())) {
//                msg = "账号不可用，如有疑问请咨询。 email: cxh1960497367@163.com";
//            }
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

    private String sendMail(User user) {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(mailU, "Xingchen's Blog", "UTF-8"));
            helper.setTo(user.getEmail());
            helper.setSubject("帐号激活:Xingchen's Blog");

            StringBuffer sb = new StringBuffer();
            //读取文件
            String path = this.getClass().getResource("/").getPath().concat("/static/ftl/mail");
            FileInputStream is = new FileInputStream(path);
            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            while ((byteread = is.read(tempbytes)) != -1) {
                sb.append(new String(tempbytes, 0, byteread));
            }

            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String url = confirmUrl.concat("?confirm_id=" + user.getConfirm_id()).concat("&email=" + user.getEmail());
            //替换
            String mail = sb.toString().replace("{name}", user.getName())
                    .replace("{url}", url)
                    .replace("{time}", time);

            helper.setText(mail, true);

            //建立线程
            MimeMessage finalMessage = message;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        javaMailSender.send(finalMessage);
                    } catch (Exception e) {
                        LOG.error("系统错误,邮件发送失败，发生异常", e);
                        //将用户信息置为不可用
                        user.setState(User.STATE_INVAITED);
                        try {
                            authService.update(user);
                        } catch (Exception e1) {
                            LOG.error("系统错误,用户信息更新失败，发生异常", e);
                        }
                    }
                }
            });
            thread.start();

            LOG.info(thread.getName() + "正在给 " + user.getEmail() + "邮箱发激活邮件.");
            return Constants.STATE_SUCCESS;
        } catch (Exception e) {
            //将用户信息置为不可用
            user.setState(User.STATE_INVAITED);
            try {
                authService.update(user);
            } catch (Exception e1) {
                LOG.error("系统错误,用户信息更新失败，发生异常", e);
            }
            return "系统错误";
        }
    }

    @RequestMapping("/confirm")
    @ResponseBody
    public JSONObject confirm(User user) {

        Map<String, Object> map = new HashMap<>();
        //先查询是否注册过
        User byEmail = authService.getByEmail(user);
        if (null == byEmail) {
            map.put("success", "该账号未注册，<a href='/reg.html'>点此注册</a>");
            return new JSONObject(map);
        }
        //激活码是否正确
        if (!byEmail.getConfirm_id().equals(user.getConfirm_id())) {
            map.put("success", "激活码错误！");
            return new JSONObject(map);
        }
        if (User.STATE_ACTIVATED.equals(byEmail.getState())) {
            map.put("success", "您的帐号已经激活过，无需再次激活！");
            return new JSONObject(map);
        }
        byEmail.setState(User.STATE_ACTIVATED);
        super.update(byEmail);
        String jsonString = JSONObject.toJSONString(byEmail);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //移除密码信息，状态和激活码信息
        jsonObject.remove("state");
        jsonObject.remove("confirm_id");
        jsonObject.remove("pwd");
        map.put("success", Constants.STATE_SUCCESS);
        map.put("userInfo", jsonObject.toJSONString());
        return new JSONObject(map);
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
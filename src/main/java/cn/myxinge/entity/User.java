package cn.myxinge.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by chenxinghua on 2017/12/20.
 */
@Entity
public class User {
    public static final String STATE_UNACTIVATED = "0";//未激活
    public static final String STATE_ACTIVATED = "1";//已激活
    public static final String STATE_INVAITED = "-1";//无法使用
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String email;
    private String login;//账号
    private String pwd;
    private String name;
    private String html_url;
    private String avatar_url;
    private Date created_at;
    private Date updated_at;
    private String confirm_id;
    private String state;
    private String isxing;  //是否是第三方登录？是为空，不是为1


    public String getIsxing() {
        return isxing;
    }

    public void setIsxing(String isxing) {
        this.isxing = isxing;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirm_id() {
        return confirm_id;
    }

    public void setConfirm_id(String confirm_id) {
        this.confirm_id = confirm_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", html_url='" + html_url + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", state='" + state + '\'' +
                '}';
    }
}

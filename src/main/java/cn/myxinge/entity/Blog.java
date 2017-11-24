package cn.myxinge.entity;

import jdk.nashorn.internal.objects.annotations.Property;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxinghua on 2017/11/9.
 * 博客类
 */
@Entity
public class Blog {

    public static final int STATE_OFFLINE=0;
    public static final int STATE_ONLINE=1;
    public static final int STATE_INVAILD=-1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String title;
    private String subject;
    private String url;
    @Column(name = "sysyUrl")
    private String sysyUrl;
    private Date createtime;
    private Date updatetime;
    //1.上线 2.下线
    private Integer state;
    private String auth;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getSysyUrl() {
        return sysyUrl;
    }

    public void setSysyUrl(String sysyUrl) {
        this.sysyUrl = sysyUrl;
    }

}

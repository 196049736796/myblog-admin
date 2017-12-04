package cn.myxinge.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chenxinghua on 2017/11/20.
 * 资源类 -> 页面资源 ， 图片资源 和其他 静态资源等
 */
@Entity
public class Resource {
    public static String STATE_NOT_USE = "0";//无
    public static String STATE_USE = "1";//有在使用
    public static String STATE_UNUSE = "-1";//已删除
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;//ID
    private String filename;
    private String description;//资源描述
    private String url;//页面url
    private String sysyUrl;//系统url
    private String suffix;//资源后缀
    private Date createtime;
    private String state;

    private Integer blogid;//属于哪个博客下的资源 、、多对一关系

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSysyUrl() {
        return sysyUrl;
    }

    public void setSysyUrl(String sysyUrl) {
        this.sysyUrl = sysyUrl;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getBlogid() {
        return blogid;
    }

    public void setBlogid(Integer blogid) {
        this.blogid = blogid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", sysyUrl='" + sysyUrl + '\'' +
                ", suffix='" + suffix + '\'' +
                ", blogid=" + blogid +
                '}';
    }
}

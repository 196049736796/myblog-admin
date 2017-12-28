package cn.myxinge.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by chenxinghua on 2017/12/27.
 */
@Entity
public class Comments {

    //评论类型：用户和博客
    public static final String CAT_USER = "1";
    public static final String CAT_BLOG = "2";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //    private Integer pid;
    @OneToOne
    @JoinColumn(name = "pid")
    private User user;
    private Integer fid;
    private String fcat;
    private String text;
    private Integer backNum;
    private Integer likeNum;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getFcat() {
        return fcat;
    }

    public void setFcat(String fcat) {
        this.fcat = fcat;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getBackNum() {
        return backNum;
    }

    public void setBackNum(Integer backNum) {
        this.backNum = backNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

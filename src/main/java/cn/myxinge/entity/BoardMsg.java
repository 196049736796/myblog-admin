package cn.myxinge.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by XingChen on 2017/11/7.
 */
@Entity
public class BoardMsg implements Serializable {
    //id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "pid")
    private User user;

    private String text;

    private Date createtime;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}














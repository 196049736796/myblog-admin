package cn.myxinge.service;

import cn.myxinge.entity.User;

/**
 * Created by XingChen on 2017/12/20.
 */
public interface AuthService extends BaseService<User>{
    User log(User user);

    User getByEmail(User user);
}

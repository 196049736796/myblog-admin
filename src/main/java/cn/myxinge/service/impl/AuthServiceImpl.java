package cn.myxinge.service.impl;

import cn.myxinge.dao.AuthDao;
import cn.myxinge.dao.VisitIpDao;
import cn.myxinge.entity.User;
import cn.myxinge.service.AuthService;
import cn.myxinge.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by XingChen on 2017/12/20.
 */
@Service
public class AuthServiceImpl extends BaseServiceImpl<User> implements AuthService {

    @Autowired
    private AuthDao authDao;

    @Override
    public User log(User user) {
        User log = authDao.log(user.getEmail(), user.getPwd());
        log.setPwd("");
        return log;
    }

    @Autowired
    public void setBlogDao(AuthDao authDao) {
        this.authDao = authDao;
        super.setJpaRepository(authDao);
    }
}
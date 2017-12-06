package cn.myxinge.service.impl;

import cn.myxinge.dao.BoardMsgDao;
import cn.myxinge.dao.MenuDao;
import cn.myxinge.entity.Menu;
import cn.myxinge.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by XingChen on 2017/11/19.
 */
@Service
@Transactional
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {
    private MenuDao menuDao;
    @Override
    public Menu getRootMenu() {
        return menuDao.findOne("0");
    }

    @Autowired
    public void setBlogDao(MenuDao menuDao) {
        this.menuDao = menuDao;
        super.setJpaRepository(menuDao);
    }
}


package cn.myxinge.dao;

import cn.myxinge.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by XingChen on 2017/11/19.
 *
 */
public interface MenuDao extends JpaRepository<Menu,String> {
}

package cn.myxinge.dao;

import cn.myxinge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by XingChen on 2017/12/20.
 */
public interface AuthDao extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * from user where email = ? and pwd = ? limit 1", nativeQuery = true)
    User log(String email, String pwd);
}

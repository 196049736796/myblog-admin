package cn.myxinge.dao;

import cn.myxinge.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by chenxinghua on 2017/11/20.
 */
public interface ResourceDao extends JpaRepository<Resource,Integer> {
}

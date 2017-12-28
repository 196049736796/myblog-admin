package cn.myxinge.service;

import cn.myxinge.entity.Blog;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * Created by XingChen on 2017/11/19.
 */
public interface BaseService<T> {
    /*** t:条件对象*/
    Page<T> list(T t, Integer page, Integer rows, Sort sort);

    Page<T> listOnWhere(T t, Integer page, Integer rows, Sort sort, Example ex);

    Long getCount(Example ex);

    void add(T t) throws Exception;

    void delete(Serializable id);

    void update(T t) throws Exception;

    T getById(Serializable id);

    List all(Example ex, Sort sort);
}

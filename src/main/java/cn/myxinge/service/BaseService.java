package cn.myxinge.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

/**
 * Created by XingChen on 2017/11/19.
 */
public interface BaseService<T> {
    /*** t:条件对象*/
    Page<T> list(T t, Integer page, Integer rows, Sort sort);

    Page<T> listOnWhere(T t, Integer page, Integer rows, Sort sort, Example ex);

    Long getCount(T t);
    void add(T t);
    void delete(Serializable id);
    void update(T t);
}

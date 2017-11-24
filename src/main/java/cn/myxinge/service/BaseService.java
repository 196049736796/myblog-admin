package cn.myxinge.service;

import org.springframework.data.domain.Page;

/**
 * Created by XingChen on 2017/11/19.
 */
public interface BaseService<T> {
    Page<T> list(T add, T update, Integer page, Integer rows);
    Long getCount(T t);
}

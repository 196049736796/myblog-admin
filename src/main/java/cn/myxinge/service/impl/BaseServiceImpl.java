package cn.myxinge.service.impl;

import cn.myxinge.dao.*;
import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by XingChen on 2017/11/19.
 */
public class BaseServiceImpl<T> implements BaseService<T> {
    private JpaRepository jpaRepository;

    private Class persistentClass;

    public BaseServiceImpl() {
        //此时获得该dao到底是那一个dao
        //getClass() 返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的超类的 Class。
        this.persistentClass = (Class<T>) getSuperClassGenricType(getClass(), 0);
    }

    @Override
    public void add(T t) {
        jpaRepository.save(t);
    }

    @Override
    public void delete(Serializable id) {
        jpaRepository.delete(id);
    }

    @Override
    public void update(T t) {
        add(t);
    }

    /**无条件查询（有排序）*/
    @Override
    public Page<T> list(T t, Integer page, Integer rows, Sort sort) {
        return listOnWhere(t, page, rows, sort, null);
    }

    /**条件查询*/
    @Override
    public Page<T> listOnWhere(T t, Integer page, Integer rows, Sort sort, Example ex) {
        int firstP = (page - 1);
        Pageable pageable = null;
        if (null != sort) {
            pageable = new PageRequest(firstP, rows, sort);
        } else {
            pageable = new PageRequest(firstP, rows);
        }

        if (null == ex) {
            return jpaRepository.findAll(pageable);
        } else {
            return jpaRepository.findAll(ex, pageable);
        }
    }

    @Override
    public Long getCount(Example ex) {
        if(null == ex){
            return jpaRepository.count();
        }
        return jpaRepository.count(ex);
    }

    @Override
    public T getById(Serializable id) {
        return (T) jpaRepository.findOne(id);
    }

    /**
     * 获取运行时泛型
     *
     * @param baseDao
     */
    public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {

        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    public void setJpaRepository(JpaRepository jpaRepository) {
        System.out.println(jpaRepository);
        this.jpaRepository = jpaRepository;
    }
}





















package cn.myxinge.service;

import cn.myxinge.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by chenxinghua on 2017/11/9.
 */
public interface BlogService {

    /**
     * 根据主键获取博客
     *
     * @param url
     * @return
     */
    Blog getBlog(String url);

    /**
     * 获取所有博客
     *
     * @return
     */
    List<Blog> allBlog();

    /**
     * 获取最新博客  n 篇
     */
    Page<Blog> newerBlog(int n);

    /**
     * 分页
     * @param add
     * @param update
     * @param page
     * @param rows
     * @return
     */
    Page<Blog> list(Blog add, Blog update, Integer page, Integer rows);

    /**
     * 数量
     * @param t
     * @return
     */
    Long getCount(Blog t);

    /**
     * 添加博客
     * @param html
     * @param blog
     */
    int addBlog(Blog blog);

    /**
     * 主键查询
     * @param id
     * @return
     */
    Blog getBlogById(Integer id);

    /**
     * 主键删除
     * @param id
     */
    void delete(Integer id);

    /**
     * 补考率状态获取列表
     * @param add
     * @param update
     * @param page
     * @param rows
     * @return
     */
    Page listWithoutState(Blog add, Blog update, Integer page, Integer rows);

    /**
     * 保存 仅存储数据库，无上传文件
     * @param blog
     */
    void save(Blog blog);
}

















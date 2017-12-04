package cn.myxinge.service;

import cn.myxinge.entity.Resource;
import org.springframework.data.domain.Page;

import java.io.InputStream;

/**
 * Created by chenxinghua on 2017/11/20.
 */
public interface ResourceService {

    String upload(InputStream is,Resource resource);

    /**
     * 上传
     * @param fileName
     * @param suffix
     * @return
     * @throws Exception
     */
    String upload(String fileName, String suffix) throws Exception;

    /**
     * 删除所有跟搞博客相关的静态资源
     * @param resource
     * @return
     * @throws Exception
     */
    String deleteWithBlog(Resource resource) throws Exception;

    /**
     * 删除Html页面
     * @param resource
     * @return
     */
    String deleteHtml(Resource resource) throws Exception;

    /**
     * 保存，针对mysql数据库
     * @param resource
     */
    public void save(Resource resource);

    /**
     * 列表显示
     * @param page
     * @param rows
     * @return
     */
    Page<Resource> list(Integer page, Integer rows);

    /**
     * 获取数据条目数量
     * @param resource 条件
     * @return
     */
    long getCount(Resource resource);

    Resource getById(Integer id);

    /**
     * 删除系统文件
     * @param r
     */
    String deleteSysFile(Resource r) throws Exception;
}

package cn.myxinge.service;

import cn.myxinge.entity.Resource;
import org.springframework.data.domain.Page;

import java.io.InputStream;

/**
 * Created by chenxinghua on 2017/11/20.
 */
public interface ResourceService extends BaseService<Resource>{

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
     * 删除系统文件
     * @param r
     */
    String deleteSysFile(Resource r) throws Exception;

    /**下载文本文件*/
    String dowloadTextFile(String sysPath) throws Exception;
}

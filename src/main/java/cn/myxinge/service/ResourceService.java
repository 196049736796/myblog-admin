package cn.myxinge.service;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import org.springframework.web.multipart.MultipartFile;

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

    public void save(Resource resource);
}

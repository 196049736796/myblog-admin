package cn.myxinge.service.impl;

import cn.myxinge.dao.ResourceDao;
import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.FastDFSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxinghua on 2017/11/20.
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private Logger LOG = LoggerFactory.getLogger(ResourceServiceImpl.class);
    @Value("${fastdfs.trackServer.conf.path}")
    private String confPath;
    @Autowired
    private ResourceDao resourceDao;

    /**
     * 上传资源
     *
     * @return 上传结果
     */
    @Override
    public String upload(MultipartFile reso, Blog blog) {
        InputStream is = null;
        FileOutputStream out = null;
        String rootPath = this.getClass().getResource("/").getPath();
        String filePath = null;

        try {
            //缓存路径
            filePath = rootPath + "static/temp/";
            File temp = new File(filePath);
            if (!temp.exists()) {
                temp.mkdirs();
            }

            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            is = reso.getInputStream();
            out = new FileOutputStream(filePath + reso.getOriginalFilename());
            while ((byteread = is.read(tempbytes)) != -1) {
                out.write(tempbytes, 0, byteread);
            }

            //上传
            FastDFSClient fastDFSClient = null;
            fastDFSClient = new FastDFSClient(rootPath + "static/conf/client.conf");
            int dian = reso.getOriginalFilename().indexOf(".");
            String sysyUrl = fastDFSClient.uploadFile(filePath + reso.getOriginalFilename(),
                    reso.getOriginalFilename().substring(dian + 1));
            String suffix =  reso.getOriginalFilename().substring(dian + 1);

            //存储资源表
            Resource resource= new Resource();
            resource.setDescription(suffix+"文件");
            resource.setSuffix(suffix);
            resource.setSysyUrl(sysyUrl);
            resource.setBlogid(blog.getId());
            resourceDao.save(resource);
            return sysyUrl;
        } catch (Exception e) {
            LOG.error("文件上传异常：" + e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOG.error("博客上传异常：" + e.getCause());
                return "-1";
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //删除临时文件
            File file = new File(filePath + reso.getOriginalFilename());
            if (file != null) {
                file.delete();
            }
        }
        return "-1";
    }

    @Override
    public String upload(String fileName, String suffix) throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient(confPath);
        return fastDFSClient.uploadFile(fileName, suffix);
    }

    @Override
    public String deleteWithBlog(Resource resource) throws Exception {
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("blogid", ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");

        Example<Resource> example = Example.of(resource, matcher);
        List<Resource> all = resourceDao.findAll(example);

        //删除失败集合
        List<Resource> delFailList = new ArrayList<>();
        FastDFSClient fastDFSClient = new FastDFSClient(confPath);

        for (Resource r : all) {
            String sysyUrl = r.getSysyUrl();
            if (null != sysyUrl) {
                int i = fastDFSClient.deleteFile(sysyUrl);
                if (0 != i) {
                    delFailList.add(r);
                } else {
                    //删除数据库记录
                    resourceDao.delete(r);
                }
            }
        }

        //组合回复信息
        if (delFailList.size() > 0) {
            String msg = "有资源删除失败，资源列表：   " + delFailList;
            return msg;
        }

        return "success";
    }

    @Override
    public void save(Resource resource) {
        resourceDao.save(resource);
    }

    @Override
    public String deleteHtml(Resource resource) throws Exception {
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("blogid", ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withMatcher("suffix", ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");

        Example<Resource> example = Example.of(resource, matcher);
        List<Resource> all = resourceDao.findAll(example);
        if(all.size() == 0){
            //之前没有页面资源
            return "none pages";
        }
        FastDFSClient fastDFSClient = new FastDFSClient(confPath);
        int i = fastDFSClient.deleteFile(all.get(0).getSysyUrl());

        if(i != 0){
            return "delete faile";
        }

        //数据库删除记录
        resourceDao.delete(all.get(0));

        return "success";
    }
}














package cn.myxinge.service.impl;

import cn.myxinge.dao.ResourceDao;
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

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxinghua on 2017/11/20.
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements ResourceService {

    private Logger LOG = LoggerFactory.getLogger(ResourceServiceImpl.class);
    private ResourceDao resourceDao;


    /**
     * 上传资源
     *
     * @return 上传结果
     */
    @Override
    public String upload(InputStream is,Resource resource) {
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
            out = new FileOutputStream(filePath + resource.getFilename());
            while ((byteread = is.read(tempbytes)) != -1) {
                out.write(tempbytes, 0, byteread);
            }

            //上传
            FastDFSClient fastDFSClient = null;
            fastDFSClient = new FastDFSClient();
            int dian = resource.getFilename().indexOf(".");
            String sysyUrl = fastDFSClient.uploadFile(filePath + resource.getFilename(),
                    resource.getFilename().substring(dian + 1));
            String suffix = resource.getFilename().substring(dian + 1);

            //存储资源表
            resource.setSuffix(suffix);
            resource.setDescription(suffix + "文件");
            resource.setSysyUrl(sysyUrl);
            resource.setState(Resource.STATE_USE);
            resource.setCreatetime(new Date());
            resourceDao.save(resource);
            return sysyUrl;
        } catch (Exception e) {
            LOG.error("文件上传异常：" + e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LOG.error("博客上传异常：" + e);
                return "-1";
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //删除临时文件
            File file = new File(filePath + resource.getFilename());
            if (file != null) {
                file.delete();
            }
        }
        return "-1";
    }

    @Override
    public String upload(String fileName, String suffix) throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient();
        return fastDFSClient.uploadFile(fileName, suffix);
    }

    @Override
    public String deleteWithBlog(Resource resource) throws Exception {
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withMatcher("blogid", ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withMatcher("state", ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");

        //只有正在使用的才可以删除
        resource.setState(Resource.STATE_USE);
        Example<Resource> example = Example.of(resource, matcher);
        List<Resource> all = resourceDao.findAll(example);

        //删除失败集合
        List<Resource> delFailList = new ArrayList<>();
        FastDFSClient fastDFSClient = new FastDFSClient();

        for (Resource r : all) {
            String sysyUrl = r.getSysyUrl();
            if (null != sysyUrl) {
                int i = fastDFSClient.deleteFile(sysyUrl);
                if (0 != i) {
                    delFailList.add(r);
                } else {
                    //删除数据库记录
//                    resourceDao.delete(r);
                    r.setState(Resource.STATE_UNUSE);
                    resourceDao.save(r);
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
    public String deleteSysFile(Resource r) throws Exception {

        FastDFSClient fastDFSClient = new FastDFSClient();
        int i = fastDFSClient.deleteFile(r.getSysyUrl());

        if (i != 0) {
            return "delete faile";
        }

        //数据库更新

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("sysyUrl", ExampleMatcher.GenericPropertyMatchers.caseSensitive());

        List<Resource> list = resourceDao.findAll(Example.of(r, matcher));

        r = list.size() > 0 ? list.get(0) : null;
        r.setState(Resource.STATE_UNUSE);
        super.update(r);
        return "success";
    }


    @Autowired
    public void setBlogDao(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
        super.setJpaRepository(resourceDao);
    }

    @Override
    public String dowloadTextFile(String sysPath) throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient();
        return new String(fastDFSClient.download_file(sysPath));
    }
}














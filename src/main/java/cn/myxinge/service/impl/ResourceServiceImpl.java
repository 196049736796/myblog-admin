package cn.myxinge.service.impl;

import cn.myxinge.dao.ResourceDao;
import cn.myxinge.entity.Resource;
import cn.myxinge.service.ResourceService;
import cn.myxinge.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenxinghua on 2017/11/20.
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Value("${fastdfs.trackServer.conf.path}")
    private String confPath;
    @Autowired
    private ResourceDao resourceDao;

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














package cn.myxinge.service;

import cn.myxinge.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by chenxinghua on 2017/11/9.
 */
public interface BlogService extends BaseService<Blog>{
    Blog getBlogByUrl(String url);
}

















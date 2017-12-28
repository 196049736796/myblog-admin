package cn.myxinge.controller;

import cn.myxinge.entity.Blog;
import cn.myxinge.entity.Comments;
import cn.myxinge.service.BlogService;
import cn.myxinge.service.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by chenxinghua on 2017/12/27.
 */
@RestController
@RequestMapping("/admin/comments")
public class CommentsController extends BaseController<Comments> {

    @Autowired
    private CommentsService commentsService;

    @RequestMapping("/add")
    @Override
    public String add(Comments comments) {
        comments.setBackNum(0);
        comments.setLikeNum(0);
        comments.setCreateTime(new Date());
        return super.add(comments);
    }

    @RequestMapping("/listByBlog")
    public List listByBlog(Integer blogId) {

        ExampleMatcher ma = ExampleMatcher.matching().withMatcher("fid",
                ExampleMatcher.GenericPropertyMatchers.caseSensitive())
                .withIgnorePaths("focus");
        Comments comments = new Comments();
        comments.setFid(blogId);
        Example<Comments> ex = Example.of(comments, ma);
        List p = commentsService.all(ex, getSort());
        return p;
    }

    @RequestMapping("/del/{id}")
    public String delete(@PathVariable Integer id) {
        return super.delete(id);
    }

    //---
    @Override
    public Sort getSort() {
        return new Sort(Sort.Direction.DESC, "createTime");
    }

    //------------------
    @Autowired
    public void setBlogService(CommentsService commentsService) {
        this.commentsService = commentsService;
        super.setBaseService(commentsService);
    }
}

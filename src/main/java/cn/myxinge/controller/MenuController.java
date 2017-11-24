package cn.myxinge.controller;

import cn.myxinge.entity.Menu;
import cn.myxinge.service.MenuService;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by XingChen on 2017/11/19.
 */
@RestController
@RequestMapping("/page")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/menu_getMenuByRole",method = {RequestMethod.GET})
    public Menu getRootMenu(){
        Menu rootMenu = menuService.getRootMenu();
        //拷贝一份；如果不拷贝，该对象已进入持久化，将会修改数据库
        Menu _rootMenu = rootMenu;
        return _rootMenu;
    }
}















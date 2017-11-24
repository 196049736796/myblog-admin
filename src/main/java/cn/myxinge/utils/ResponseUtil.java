package cn.myxinge.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by XingChen on 2017/11/19.
 */
public class ResponseUtil {

    /**
     * 返回字符串 文本
     * @param success
     * @param msg
     * @return
     */
    public static String returnMsg(boolean success, String msg) {
        JSONObject rtn = new JSONObject();
        rtn.put("success", success);
        rtn.put("message", msg);
        return rtn.toJSONString();
    }

    /**
     * 返回Json对象
     */
    public static JSONObject returnJson(boolean success, String msg) {
        JSONObject rtn = new JSONObject();
        rtn.put("success", success);
        rtn.put("message", msg);
        return rtn;
    }
}

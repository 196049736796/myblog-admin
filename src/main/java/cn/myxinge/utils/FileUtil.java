package cn.myxinge.utils;

import org.hibernate.annotations.Check;
import org.springframework.util.StringUtils;

import java.time.temporal.Temporal;

/**
 * Created by chenxinghua on 2017/12/2.
 */
public class FileUtil {
    //判断是否是图片
    public static boolean isPicture(String  pInput){
        // 文件名称为空的场合
        if(StringUtils.isEmpty(pInput)){
            // 返回不和合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1,
                pInput.length());
        // 声明图片后缀名数组
        String imgeArray [][] = {
                {"bmp", "0"}, {"dib", "1"}, {"gif", "2"},
                {"jfif", "3"}, {"jpe", "4"}, {"jpeg", "5"},
                {"jpg", "6"}, {"png", "7"} ,{"tif", "8"},
                {"tiff", "9"}, {"ico", "10"}
        };
        boolean isP = false;
        // 遍历名称数组
        for(int i = 0; i<imgeArray.length;i++){
           if(imgeArray[i].equals(tmpName.toLowerCase())){
               isP = true;
           }
        }
        return isP;
    }
}

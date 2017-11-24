package cn.myxinge.common;

import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenxinghua on 2017/11/21.
 *  自定义LOG输出类，控制LOG文件路径
 */
public class MyLogHandler extends TimeBasedRollingPolicy {
    private Logger LOG = LoggerFactory.getLogger(MyLogHandler.class);
    private String fileNamePatternStr;

    public MyLogHandler(){
        System.err.println("-------------------------->MyLogHandler init...");
    }

    @Override
    public void start() {
        this.fileNamePatternStr = super.getFileNamePattern().
                replace("classpath:",this.getClass().getResource("/").getPath());
        super.setFileNamePattern(this.fileNamePatternStr);

        System.err.println("-------------------------->Log filePath: "+this.fileNamePatternStr);
        super.start();
    }
}















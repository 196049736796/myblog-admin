package cn.myxinge.utils;


import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.IOException;

/**
 * Created by XingChen on 2017/11/18.
 * <p>
 * 文件上传到FastDFS工具类
 */
public class FastDFSClient {
    private TrackerClient trackerClient = null;
    private TrackerServer trackerServer = null;
    private StorageServer storageServer = null;
    private StorageClient1 storageClient = null;

    public FastDFSClient(String conf) throws Exception {
        if (conf.contains("classpath:")) {
            conf = conf.replace("classpath:", this.getClass().getResource("/").getPath());
        }
        ClientGlobal.init(conf);
        trackerClient = new TrackerClient();
        trackerServer = trackerClient.getConnection();
        storageServer = null;
        storageClient = new StorageClient1(trackerServer, storageServer);
    }

    /**
     * 上传文件方法
     * <p>Title: uploadFile</p>
     * <p>Description: </p>
     *
     * @param fileName 文件全路径
     * @param extName  文件扩展名，不包含（.）
     * @param metas    文件扩展信息
     * @return
     * @throws Exception
     */
    public String uploadFile(String fileName, String extName, NameValuePair[] metas) throws Exception {
        String result = storageClient.upload_file1(fileName, extName, metas);
        return result;
    }


    public String uploadFile(String fileName) throws Exception {
        return uploadFile(fileName, null, null);
    }

    public String uploadFile(String fileName, String extName) throws Exception {
        return uploadFile(fileName, extName, null);
    }

    /**
     * 删除文件
     * @param groupName 组名
     * @param remoteName 文件名
     * @return 结果
     * @throws IOException
     * @throws MyException
     */
    public int deleteFile(String groupName,String remoteName) throws IOException, MyException {
        return storageClient.delete_file(groupName,remoteName);
    }

    /**
     * 删除文件 组名+路径
     * @param storagePath
     * @return
     * @throws IOException
     * @throws MyException
     */
    public int deleteFile(String storagePath) throws IOException, MyException {
        return storageClient.delete_file1(storagePath);
    }


}

package com.luhuiguo.fastdfs.service;

import java.io.InputStream;
import java.util.Set;

import com.luhuiguo.fastdfs.domain.MetaData;
import com.luhuiguo.fastdfs.domain.StorePath;

/**
 * 面向普通应用的文件操作接口封装
 * 
 * @author tobato
 *
 */
public interface FastFileStorageClient extends GenerateStorageClient {


  
     StorePath uploadFile(byte[] content, String fileExtName);
  
    /**
     * 上传一般文件
     * 
     * @param inputStream
     * @param fileSize
     * @param fileExtName
     * @param metaDataSet
     * @return
     */
    StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName, Set<MetaData> metaDataSet);

    /**
     * 删除文件
     * 
     * @param filePath 文件路径(groupName/path)
     */
    void deleteFile(String filePath);

}

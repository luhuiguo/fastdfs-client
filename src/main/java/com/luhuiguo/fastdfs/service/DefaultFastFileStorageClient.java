package com.luhuiguo.fastdfs.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.luhuiguo.fastdfs.conn.ConnectionManager;
import com.luhuiguo.fastdfs.domain.MetaData;
import com.luhuiguo.fastdfs.domain.StorageNode;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.proto.storage.StorageSetMetadataCommand;
import com.luhuiguo.fastdfs.proto.storage.StorageUploadFileCommand;
import com.luhuiguo.fastdfs.proto.storage.enums.StorageMetdataSetType;

/**
 * 面向应用的接口实现
 * 
 * @author luhuiguo
 *
 */
public class DefaultFastFileStorageClient extends DefaultGenerateStorageClient
    implements FastFileStorageClient {

  public DefaultFastFileStorageClient(TrackerClient trackerClient,
      ConnectionManager connectionManager) {
    super(trackerClient, connectionManager);
  }


  /**
   * 上传文件
   */
  @Override
  public StorePath uploadFile(InputStream inputStream, long fileSize, String fileExtName,
      Set<MetaData> metaDataSet) {
    Validate.notNull(inputStream, "上传文件流不能为空");
    Validate.notBlank(fileExtName, "文件扩展名不能为空");
    StorageNode client = trackerClient.getStoreStorage();
    return uploadFileAndMetaData(client, inputStream, fileSize, fileExtName, metaDataSet);
  }


  /**
   * 检查是否有MetaData
   * 
   * @param metaDataSet
   * @return
   */
  private boolean hasMetaData(Set<MetaData> metaDataSet) {
    return null != metaDataSet && !metaDataSet.isEmpty();
  }

  /**
   * 上传文件和元数据
   * 
   * @param client
   * @param inputStream
   * @param fileSize
   * @param fileExtName
   * @param metaDataSet
   * @return
   */
  private StorePath uploadFileAndMetaData(StorageNode client, InputStream inputStream,
      long fileSize,
      String fileExtName, Set<MetaData> metaDataSet) {
    // 上传文件
    StorageUploadFileCommand command =
        new StorageUploadFileCommand(client.getStoreIndex(), inputStream,
            fileExtName, fileSize, false);
    StorePath path = connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
    // 上传metadata
    if (hasMetaData(metaDataSet)) {
      StorageSetMetadataCommand setMDCommand =
          new StorageSetMetadataCommand(path.getGroup(), path.getPath(),
              metaDataSet, StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
      connectionManager.executeFdfsCmd(client.getInetSocketAddress(), setMDCommand);
    }
    return path;
  }

  /**
   * 删除文件
   */
  @Override
  public void deleteFile(String filePath) {
    StorePath storePath = StorePath.praseFromUrl(filePath);
    super.deleteFile(storePath.getGroup(), storePath.getPath());
  }


  @Override
  public StorePath uploadFile(byte[] content, String fileExtName) {

    return uploadFile(new ByteArrayInputStream(content), content.length, fileExtName, null);
  }

}

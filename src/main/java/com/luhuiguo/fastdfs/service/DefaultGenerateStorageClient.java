package com.luhuiguo.fastdfs.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luhuiguo.fastdfs.conn.ConnectionManager;
import com.luhuiguo.fastdfs.domain.FileInfo;
import com.luhuiguo.fastdfs.domain.MetaData;
import com.luhuiguo.fastdfs.domain.StorageNode;
import com.luhuiguo.fastdfs.domain.StorageNodeInfo;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.proto.storage.DownloadByteArray;
import com.luhuiguo.fastdfs.proto.storage.DownloadCallback;
import com.luhuiguo.fastdfs.proto.storage.StorageDeleteFileCommand;
import com.luhuiguo.fastdfs.proto.storage.StorageDownloadCommand;
import com.luhuiguo.fastdfs.proto.storage.StorageGetMetadataCommand;
import com.luhuiguo.fastdfs.proto.storage.StorageQueryFileInfoCommand;
import com.luhuiguo.fastdfs.proto.storage.StorageSetMetadataCommand;
import com.luhuiguo.fastdfs.proto.storage.StorageUploadFileCommand;
import com.luhuiguo.fastdfs.proto.storage.StorageUploadSlaveFileCommand;
import com.luhuiguo.fastdfs.proto.storage.enums.StorageMetdataSetType;

/**
 * 基本存储客户端操作实现
 * 
 * @author tobato
 *
 */
public class DefaultGenerateStorageClient implements GenerateStorageClient {

  /** trackerClient */
  protected TrackerClient trackerClient;

  /** connectManager */
  protected ConnectionManager connectionManager;

  /** 日志 */
  protected static Logger LOGGER = LoggerFactory.getLogger(DefaultGenerateStorageClient.class);



  public DefaultGenerateStorageClient(TrackerClient trackerClient,
      ConnectionManager connectionManager) {
    super();
    this.trackerClient = trackerClient;
    this.connectionManager = connectionManager;
  }

  /**
   * 上传不支持断点续传的文件
   */
  @Override
  public StorePath uploadFile(String groupName, InputStream inputStream, long fileSize,
      String fileExtName) {
    StorageNode client = trackerClient.getStoreStorage(groupName);
    StorageUploadFileCommand command =
        new StorageUploadFileCommand(client.getStoreIndex(), inputStream,
            fileExtName, fileSize, false);
    return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
  }

  /**
   * 上传从文件
   */
  @Override
  public StorePath uploadSlaveFile(String groupName, String masterFilename, InputStream inputStream,
      long fileSize,
      String prefixName, String fileExtName) {
    StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, masterFilename);
    StorageUploadSlaveFileCommand command =
        new StorageUploadSlaveFileCommand(inputStream, fileSize, masterFilename,
            prefixName, fileExtName);
    return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
  }

  /**
   * 获取metadata
   */
  @Override
  public Set<MetaData> getMetadata(String groupName, String path) {
    StorageNodeInfo client = trackerClient.getFetchStorage(groupName, path);
    StorageGetMetadataCommand command = new StorageGetMetadataCommand(groupName, path);
    return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
  }

  /**
   * 覆盖metadata
   */
  @Override
  public void overwriteMetadata(String groupName, String path, Set<MetaData> metaDataSet) {
    StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
    StorageSetMetadataCommand command = new StorageSetMetadataCommand(groupName, path, metaDataSet,
        StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_OVERWRITE);
    connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
  }

  /**
   * 合并metadata
   */
  @Override
  public void mergeMetadata(String groupName, String path, Set<MetaData> metaDataSet) {
    StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
    StorageSetMetadataCommand command = new StorageSetMetadataCommand(groupName, path, metaDataSet,
        StorageMetdataSetType.STORAGE_SET_METADATA_FLAG_MERGE);
    connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
  }

  /**
   * 查询文件信息
   */
  @Override
  public FileInfo queryFileInfo(String groupName, String path) {
    StorageNodeInfo client = trackerClient.getFetchStorage(groupName, path);
    StorageQueryFileInfoCommand command = new StorageQueryFileInfoCommand(groupName, path);
    return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
  }

  /**
   * 删除文件
   */
  @Override
  public void deleteFile(String groupName, String path) {
    StorageNodeInfo client = trackerClient.getUpdateStorage(groupName, path);
    StorageDeleteFileCommand command = new StorageDeleteFileCommand(groupName, path);
    connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
  }

  /**
   * 下载整个文件
   */
  @Override
  public <T> T downloadFile(String groupName, String path, DownloadCallback<T> callback) {
    long fileOffset = 0;
    long fileSize = 0;
    return downloadFile(groupName, path, fileOffset, fileSize, callback);
  }

  /**
   * 下载文件片段
   */
  @Override
  public <T> T downloadFile(String groupName, String path, long fileOffset, long fileSize,
      DownloadCallback<T> callback) {
    StorageNodeInfo client = trackerClient.getFetchStorage(groupName, path);
    StorageDownloadCommand<T> command =
        new StorageDownloadCommand<T>(groupName, path, 0, 0, callback);
    return connectionManager.executeFdfsCmd(client.getInetSocketAddress(), command);
  }

  public void setTrackerClientService(TrackerClient trackerClientService) {
    this.trackerClient = trackerClientService;
  }

  public void setConnectionManager(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }


  @Override
  public StorePath uploadFile(String groupName, byte[] content, String fileExtName) {

    return uploadFile(groupName, new ByteArrayInputStream(content), content.length, fileExtName);
  }

  @Override
  public byte[] downloadFile(String groupName, String path) {
    return downloadFile(groupName, path, new DownloadByteArray());
  }

}

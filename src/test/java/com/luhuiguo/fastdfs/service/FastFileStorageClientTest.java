package com.luhuiguo.fastdfs.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.luhuiguo.fastdfs.conn.ConnectionManager;
import com.luhuiguo.fastdfs.conn.ConnectionPoolConfig;
import com.luhuiguo.fastdfs.conn.FdfsConnectionPool;
import com.luhuiguo.fastdfs.conn.PooledConnectionFactory;
import com.luhuiguo.fastdfs.conn.TrackerConnectionManager;
import com.luhuiguo.fastdfs.domain.MetaData;
import com.luhuiguo.fastdfs.domain.StorePath;

public class FastFileStorageClientTest {
  

  protected static Logger LOGGER = LoggerFactory.getLogger(FastFileStorageClientTest.class);


  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testUploadFileInputStreamLongStringSetOfMetaData() throws Exception {

    FdfsConnectionPool pool = new FdfsConnectionPool();
    List<String> trackers = new ArrayList<>();
    trackers.add("10.1.5.85:22122");
    
    TrackerConnectionManager tcm = new TrackerConnectionManager(pool,trackers);
    TrackerClient trackerClient = new DefaultTrackerClient(tcm);
    
    ConnectionManager cm = new ConnectionManager(pool);
    FastFileStorageClient  storageClient = new DefaultFastFileStorageClient(trackerClient,cm);
    
    LOGGER.debug("##上传文件..##");
    File file = new File("pom.xml");
    
    // Metadata
    Set<MetaData> metaDataSet = new HashSet<>();
    metaDataSet.add(new MetaData("Length", String.valueOf(file.length())));
    metaDataSet.add(new MetaData("FileName", file.getName()));
    // 上传文件和Metadata
    StorePath path = storageClient.uploadFile(new FileInputStream(file), file.length(), FilenameUtils.getExtension(file.getName()),
            metaDataSet);
    assertNotNull(path);
    LOGGER.debug("上传文件路径{}", path);

    // 验证获取MataData
    LOGGER.debug("##获取Metadata##");
    Set<MetaData> fetchMateData = storageClient.getMetadata(path.getGroup(), path.getPath());
    assertEquals(fetchMateData, metaDataSet);

    LOGGER.debug("##删除文件..##");
    storageClient.deleteFile(path.getGroup(), path.getPath());
  }

}

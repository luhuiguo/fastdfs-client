package com.luhuiguo.fastdfs.service;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.luhuiguo.fastdfs.conn.ConnectionPoolConfig;
import com.luhuiguo.fastdfs.conn.FdfsConnectionPool;
import com.luhuiguo.fastdfs.conn.PooledConnectionFactory;
import com.luhuiguo.fastdfs.conn.TrackerConnectionManager;
import com.luhuiguo.fastdfs.domain.GroupState;



public class TrackerClientTest {
  
  
  

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testListGroups() {
    
    FdfsConnectionPool pool = new FdfsConnectionPool();
    List<String> trackers = new ArrayList<>();
    trackers.add("10.1.5.85:22122");
    TrackerConnectionManager tcm = new TrackerConnectionManager(pool,trackers);
    TrackerClient client = new DefaultTrackerClient(tcm);
    List<GroupState> groups = client.listGroups();
    System.out.println(groups);
    assertNotNull(groups);
  }

}

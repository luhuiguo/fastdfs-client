package com.luhuiguo.fastdfs.conn;

import java.net.InetSocketAddress;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * 定义Fdfs连接池对象
 * 
 * <pre>
 * 定义了对象池要实现的功能,对一个地址进行池化Map Pool
 * </pre>
 * 
 * @author luhuiguo
 *
 */
public class FdfsConnectionPool extends GenericKeyedObjectPool<InetSocketAddress, Connection> {

  public FdfsConnectionPool(KeyedPooledObjectFactory<InetSocketAddress, Connection> factory,
      GenericKeyedObjectPoolConfig config) {
    super(factory, config);
  }


  public FdfsConnectionPool(KeyedPooledObjectFactory<InetSocketAddress, Connection> factory) {
    super(factory, new ConnectionPoolConfig());
  }

  public FdfsConnectionPool() {
    super(new PooledConnectionFactory(), new ConnectionPoolConfig());
  }

}

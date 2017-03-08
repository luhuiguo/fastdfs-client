# FastDFS java Client

基于 https://github.com/tobato/FastDFS_Client

拆分成 **fastdfs-client** 和 **fastdfs-spring-boot-starter**

## Not Spring Boot-based project


```xml
		<dependency>
			<groupId>com.luhuiguo</groupId>
			<artifactId>fastdfs-client</artifactId>
			<version>0.3.0</version>
		</dependency>
```

```java
    FdfsConnectionPool pool = new FdfsConnectionPool();
    List<String> trackers = new ArrayList<>();
    trackers.add("10.1.5.85:22122");

    TrackerConnectionManager tcm = new TrackerConnectionManager(pool, trackers);
    TrackerClient trackerClient = new DefaultTrackerClient(tcm);

    ConnectionManager cm = new ConnectionManager(pool);
    FastFileStorageClient storageClient = new DefaultFastFileStorageClient(trackerClient, cm);
```


## Spring Boot-based project

```xml
		<dependency>
			<groupId>com.luhuiguo</groupId>
			<artifactId>fastdfs-spring-boot-starter</artifactId>
			<version>0.1.0</version>
		</dependency>
```

```yml
fdfs:
  connect-timeout: 2000
  so-timeout: 3000
  tracker-list:
    - 10.1.5.85:22122
    - 10.1.5.86:22122
    
```

```java
  @Autowired
  private FastFileStorageClient storageClient;
```

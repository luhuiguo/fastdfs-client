package com.luhuiguo.fastdfs.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.luhuiguo.fastdfs.conn.TrackerConnectionManager;
import com.luhuiguo.fastdfs.domain.GroupState;
import com.luhuiguo.fastdfs.domain.StorageNode;
import com.luhuiguo.fastdfs.domain.StorageNodeInfo;
import com.luhuiguo.fastdfs.domain.StorageState;
import com.luhuiguo.fastdfs.proto.tracker.TrackerDeleteStorageCommand;
import com.luhuiguo.fastdfs.proto.tracker.TrackerGetFetchStorageCommand;
import com.luhuiguo.fastdfs.proto.tracker.TrackerGetStoreStorageCommand;
import com.luhuiguo.fastdfs.proto.tracker.TrackerListGroupsCommand;
import com.luhuiguo.fastdfs.proto.tracker.TrackerListStoragesCommand;

/**
 * 目录服务客户端默认实现
 * 
 * @author tobato
 *
 */
public class DefaultTrackerClient implements TrackerClient {

    private TrackerConnectionManager trackerConnectionManager;

    public DefaultTrackerClient(TrackerConnectionManager trackerConnectionManager) {
      super();
      this.trackerConnectionManager = trackerConnectionManager;
    }

    /**
     * 获取存储节点
     */
    @Override
    public StorageNode getStoreStorage() {
        TrackerGetStoreStorageCommand command = new TrackerGetStoreStorageCommand();
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 按组获取存储节点
     */
    @Override
    public StorageNode getStoreStorage(String groupName) {
        TrackerGetStoreStorageCommand command;
        if (StringUtils.isBlank(groupName)) {
            command = new TrackerGetStoreStorageCommand();
        } else {
            command = new TrackerGetStoreStorageCommand(groupName);
        }

        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 获取源服务器
     */
    @Override
    public StorageNodeInfo getFetchStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, false);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 获取更新服务器
     */
    @Override
    public StorageNodeInfo getUpdateStorage(String groupName, String filename) {
        TrackerGetFetchStorageCommand command = new TrackerGetFetchStorageCommand(groupName, filename, true);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 列出组
     */
    @Override
    public List<GroupState> listGroups() {
        TrackerListGroupsCommand command = new TrackerListGroupsCommand();
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 按组列出存储状态
     */
    @Override
    public List<StorageState> listStorages(String groupName) {
        TrackerListStoragesCommand command = new TrackerListStoragesCommand(groupName);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 按ip列出存储状态
     */
    @Override
    public List<StorageState> listStorages(String groupName, String storageIpAddr) {
        TrackerListStoragesCommand command = new TrackerListStoragesCommand(groupName, storageIpAddr);
        return trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

    /**
     * 删除存储节点
     */
    @Override
    public void deleteStorage(String groupName, String storageIpAddr) {
        TrackerDeleteStorageCommand command = new TrackerDeleteStorageCommand(groupName, storageIpAddr);
        trackerConnectionManager.executeFdfsTrackerCmd(command);
    }

}

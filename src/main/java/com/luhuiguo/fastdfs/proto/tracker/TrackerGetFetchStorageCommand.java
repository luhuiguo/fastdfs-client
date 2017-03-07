package com.luhuiguo.fastdfs.proto.tracker;

import com.luhuiguo.fastdfs.domain.StorageNodeInfo;
import com.luhuiguo.fastdfs.proto.AbstractFdfsCommand;
import com.luhuiguo.fastdfs.proto.FdfsResponse;
import com.luhuiguo.fastdfs.proto.tracker.internal.TrackerGetFetchStorageRequest;

/**
 * 获取源服务器
 * 
 * @author tobato
 *
 */
public class TrackerGetFetchStorageCommand extends AbstractFdfsCommand<StorageNodeInfo> {

    public TrackerGetFetchStorageCommand(String groupName, String path, boolean toUpdate) {
        super.request = new TrackerGetFetchStorageRequest(groupName, path, toUpdate);
        super.response = new FdfsResponse<StorageNodeInfo>() {
            // default response
        };
    }

}

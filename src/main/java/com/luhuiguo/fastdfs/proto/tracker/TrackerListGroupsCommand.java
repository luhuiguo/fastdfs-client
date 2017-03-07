package com.luhuiguo.fastdfs.proto.tracker;

import java.util.List;

import com.luhuiguo.fastdfs.domain.GroupState;
import com.luhuiguo.fastdfs.proto.AbstractFdfsCommand;
import com.luhuiguo.fastdfs.proto.tracker.internal.TrackerListGroupsRequest;
import com.luhuiguo.fastdfs.proto.tracker.internal.TrackerListGroupsResponse;

/**
 * 列出组命令
 * 
 * @author tobato
 *
 */
public class TrackerListGroupsCommand extends AbstractFdfsCommand<List<GroupState>> {

    public TrackerListGroupsCommand() {
        super.request = new TrackerListGroupsRequest();
        super.response = new TrackerListGroupsResponse();
    }

}

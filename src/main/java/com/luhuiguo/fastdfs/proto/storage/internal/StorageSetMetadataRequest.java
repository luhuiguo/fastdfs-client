package com.luhuiguo.fastdfs.proto.storage.internal;

import java.nio.charset.Charset;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.luhuiguo.fastdfs.domain.MetaData;
import com.luhuiguo.fastdfs.proto.CmdConstants;
import com.luhuiguo.fastdfs.proto.FdfsRequest;
import com.luhuiguo.fastdfs.proto.OtherConstants;
import com.luhuiguo.fastdfs.proto.ProtoHead;
import com.luhuiguo.fastdfs.proto.mapper.DynamicFieldType;
import com.luhuiguo.fastdfs.proto.mapper.FdfsColumn;
import com.luhuiguo.fastdfs.proto.mapper.MetadataMapper;
import com.luhuiguo.fastdfs.proto.storage.enums.StorageMetdataSetType;

/**
 * 设置文件标签
 * 
 * @author tobato
 *
 */
public class StorageSetMetadataRequest extends FdfsRequest {

    /** 文件名byte长度 */
    @FdfsColumn(index = 0)
    private int fileNameByteLengh;
    /** 元数据byte长度 */
    @FdfsColumn(index = 1)
    private int mataDataByteLength;
    /** 操作标记（重写/覆盖） */
    @FdfsColumn(index = 2)
    private byte opFlag;
    /** 组名 */
    @FdfsColumn(index = 3, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /** 文件路径 */
    @FdfsColumn(index = 4, dynamicField = DynamicFieldType.allRestByte)
    private String path;
    /** 元数据 */
    @FdfsColumn(index = 5, dynamicField = DynamicFieldType.metadata)
    private Set<MetaData> metaDataSet;

    /**
     * 设置文件元数据
     * 
     * @param groupName
     * @param path
     * @param metaDataSet
     * @param type
     */
    public StorageSetMetadataRequest(String groupName, String path, Set<MetaData> metaDataSet,
            StorageMetdataSetType type) {
        super();
        Validate.notBlank(groupName, "分组不能为空");
        Validate.notBlank(path, "分组不能为空");
        Validate.notEmpty(metaDataSet, "分组不能为空");
        Validate.notNull(type, "标签设置方式不能为空");
        this.groupName = groupName;
        this.path = path;
        this.metaDataSet = metaDataSet;
        this.opFlag = type.getType();
        head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_SET_METADATA);
    }

    /**
     * 打包参数
     */
    @Override
    public byte[] encodeParam(Charset charset) {
        // 运行时参数在此计算值
        this.fileNameByteLengh = path.getBytes(charset).length;
        this.mataDataByteLength = getMetaDataSetByteSize(charset);
        return super.encodeParam(charset);
    }

    /**
     * 获取metaDataSet长度
     * 
     * @param metaDataSet
     * @param charset
     * @return
     */
    private int getMetaDataSetByteSize(Charset charset) {
        return MetadataMapper.toByte(metaDataSet, charset).length;
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<MetaData> getMetaDataSet() {
        return metaDataSet;
    }

    public byte getOpFlag() {
        return opFlag;
    }

    public String getPath() {
        return path;
    }

    public int getFileNameByteLengh() {
        return fileNameByteLengh;
    }

    public int getMataDataByteLength() {
        return mataDataByteLength;
    }

}

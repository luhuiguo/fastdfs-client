package com.luhuiguo.fastdfs.proto.storage.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import com.luhuiguo.fastdfs.proto.FdfsResponse;
import com.luhuiguo.fastdfs.proto.storage.DownloadCallback;
import com.luhuiguo.fastdfs.proto.storage.FdfsInputStream;

/**
 * 文件下载结果
 * 
 * @author tobato
 * @param <T>
 *
 */
public class StorageDownloadResponse<T> extends FdfsResponse<T> {

    private DownloadCallback<T> callback;

    public StorageDownloadResponse(DownloadCallback<T> callback) {
        super();
        this.callback = callback;
    }

    /**
     * 解析反馈内容
     */
    @Override
    public T decodeContent(InputStream in, Charset charset) throws IOException {
        // 解析报文内容
        FdfsInputStream input = new FdfsInputStream(in, getContentLength());
        return callback.recv(input);
    }

}

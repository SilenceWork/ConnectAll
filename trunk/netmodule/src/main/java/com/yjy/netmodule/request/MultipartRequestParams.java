package com.yjy.netmodule.request;

import org.apache.http.HttpEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class MultipartRequestParams {

    protected ConcurrentHashMap<String, String> urlParams;
    protected ConcurrentHashMap<String, FileWrapper> fileParams;

    private static class FileWrapper {
        public InputStream inputStream;
        public String fileName;
        public String contentType;

        public FileWrapper(InputStream inputStream, String fileName, String contentType) {
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public String getFileName() {
            if (fileName == null) {
                return "nofilename";
            } else {
                return fileName;
            }
        }
    }

    public MultipartRequestParams() {
        init();
    }

    public MultipartRequestParams(String key, String value) {
        init();
        put(key, value);
    }

    private void init() {
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }

    public void put(String key, File file) {
        try {
            put(key, new FileInputStream(file), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void put(String key, FileInputStream fileInputStream, String name) {
        put(key, fileInputStream, name, null);
    }

    public void put(String key, FileInputStream fileInputStream, String name, String contentType) {
        if (key != null && fileInputStream != null) {
            fileParams.put(key, new FileWrapper(fileInputStream, name, contentType));
        }
    }

    public HttpEntity getEntity() {
        MultipartEntity multipartEntity = new MultipartEntity();

        if (urlParams != null) {
            for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
                multipartEntity.addPart(entry.getKey(), entry.getValue());
            }
        }

        if (fileParams != null && !fileParams.isEmpty()) {
            int currentIndex = 0;
            int lastIndex = fileParams.entrySet().size() - 1;

            for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
                FileWrapper file = entry.getValue();
                if (file.inputStream != null) {
                    boolean isLast = currentIndex == lastIndex;
                    if (file.contentType != null) {
                        multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream, file.contentType, isLast);
                    } else {
                        multipartEntity.addPart(entry.getKey(), file.getFileName(), file.inputStream, isLast);
                    }
                }
                currentIndex++;
            }
        }
        return multipartEntity;
    }

}

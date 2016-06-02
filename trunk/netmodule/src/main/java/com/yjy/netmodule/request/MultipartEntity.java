package com.yjy.netmodule.request;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class MultipartEntity implements HttpEntity {
    private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private String boundary = null;

    ByteArrayOutputStream out = new ByteArrayOutputStream();

    boolean isSetLast = false;
    boolean isSetFirst = false;

    private static byte[] tmp = new byte[1024];

    public MultipartEntity() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Random random = new Random();
        for (int i = 0; i < 40; i++) {
            stringBuilder.append(MULTIPART_CHARS[random.nextInt(MULTIPART_CHARS.length)]);
        }
        this.boundary = stringBuilder.toString();
    }

    public void writeFirstBoundaryIfNeeds() {
        if (!isSetFirst) {
            try {
                out.write(("--" + boundary + "\r\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (isSetLast) {
            return;
        }
        try {
            out.write(("\r\n--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        isSetLast = true;
    }

    public void addPart(final String key, final String value) {
        writeFirstBoundaryIfNeeds();
        try {
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
            out.write(value.getBytes());
            out.write(("\r\n--" + boundary + "\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPart(final String key, final String fileName, final InputStream fin, String type, final boolean isLast) {
        writeFirstBoundaryIfNeeds();
        try {
            type = "Content-Type: " + type + "\r\n";
            out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
            out.write(type.getBytes());
            out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

            int l = 0;
            while ((l = fin.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }
            if (!isLast) {
                out.write(("\r\n--" + boundary + "\r\n").getBytes());
            } else {
                writeLastBoundaryIfNeeds();
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addPart(final String key, final String fileName, final InputStream fin, final boolean isLast) {
        addPart(key, fileName, fin, "application/octet-stream", isLast);
    }

    public void addPart(final String key, final File value, final boolean isLast) {
        try {
            addPart(key, value.getName(), new FileInputStream(value), isLast);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return out.toByteArray().length;
    }

    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    @Override
    public boolean isChunked() {
        return false;
    }


    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
//        outputStream.write(out.toByteArray());

        byte[] data = out.toByteArray();
        if (data != null) {
            int off = 0;
            int perSize = data.length / 10;
            if (perSize <= 0 || perSize > 4096) {
                perSize = 4096;
            }
            try {
                while (off < data.length) {
                    if ((off + perSize) >= data.length) {
                        outputStream.write(data, off, data.length - off);
                        off = data.length;
                    } else {
                        outputStream.write(data, off, perSize);
                        off += perSize;
                    }
                    outputStream.flush();
                    if(out != null) {
                        out.close();
                        out = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        data = null;
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public void consumeContent() throws IOException, UnsupportedOperationException {
        if (isStreaming()) {
            throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent");
        }
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return new ByteArrayInputStream(out.toByteArray());
    }

}

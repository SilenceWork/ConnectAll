package com.yjy.netmodule.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpEntity;

public class BaseMultipartRequest extends StringRequest {

    private MultipartRequestParams params;
    private HttpEntity httpEntity;

    public BaseMultipartRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener, MultipartRequestParams params) {
        super(Method.POST, url, listener, errorListener);
        this.params = params;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (params != null) {
            httpEntity = params.getEntity();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return httpEntity.getContentType().getValue();
    }

}

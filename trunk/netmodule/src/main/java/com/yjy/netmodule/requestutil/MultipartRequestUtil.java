package com.yjy.netmodule.requestutil;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.util.Map;

import com.yjy.netmodule.callback.CallBackListener;
import com.yjy.netmodule.request.BaseMultipartRequest;
import com.yjy.netmodule.request.MultipartRequestParams;

public class MultipartRequestUtil extends BaseRequestUtil {
    private static final int MAX_UPLOAD_FILE_NUM = 3;
    private BaseMultipartRequest multipartRequest;

    public MultipartRequestUtil(Context context) {
        super(context);
    }

    public void cancel() {
        if (multipartRequest != null && !multipartRequest.isCanceled()) {
            multipartRequest.cancel();
        }
    }

    public void upload(String url, Map<String, String> params, Map<String, File> fileParams, final CallBackListener<String> listener) {
//        Methods.setCommonParams(params);
//
//        int flag = Methods.getUrlEnt(url);
//        if(flag == 2) {
//            // 接入AOS系统，对登录信息进行XXTea加密
//            params = XXTeaHelper.getInstance().encryptAndEncodeToPost(params, true);
//        } else if(flag == 1) {
//            // 登录以外的接口进行AES加密
//            params = AESHelper.getInstance().encryptAndHexToPost(params);
//        } else {
//            // 不加密
//        }

        MultipartRequestParams multipartRequestParams = new MultipartRequestParams();

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                multipartRequestParams.put(entry.getKey(), entry.getValue());
            }
        }

        if (fileParams != null && !fileParams.isEmpty()) {
            for (Map.Entry<String, File> entry : fileParams.entrySet()) {
                multipartRequestParams.put(entry.getKey(), entry.getValue());
            }
        }

        multipartRequest = new BaseMultipartRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //GDLog.v("upload","upload response:" + response);
                        if (listener != null) {
                            listener.onSuccessResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //GDLog.v("upload","upload error:" + error);
                        if (listener != null) {
                            listener.onErrorResponse(error);
                        }
                    }
                }, multipartRequestParams);
        addRequest(multipartRequest);
    }
}

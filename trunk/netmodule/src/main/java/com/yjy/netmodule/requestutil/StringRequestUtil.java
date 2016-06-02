package com.yjy.netmodule.requestutil;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yjy.netmodule.callback.CallBackListener;
import com.yjy.netmodule.request.BaseStringRequest;

import java.util.Map;

public class StringRequestUtil extends BaseRequestUtil<String> {

    private BaseStringRequest stringRequest;

    public StringRequestUtil(Context context) {
        super(context);
    }

    public void get(String url, final CallBackListener<String> listener) {
        stringRequest = new BaseStringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (listener != null) {
                            listener.onSuccessResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null) {
                            listener.onErrorResponse(error);
                        }
                    }
                });
        addRequest(stringRequest);
    }

    public void get(String url, final CallBackListener<String> listener, Map<String, String> params) {
        //Methods.setCommonParams(params);

//        int flag = Methods.getUrlEnt(url);
//        if (flag == 2) {
//            url += "?" + XXTeaHelper.getInstance().encryptAndEncodeToGet(params);
//        } else if (flag == 1) {
//            url += "?" + AESHelper.getInstance().encryptAndHexToGet(params);
//        } else {
            url = createGetUrlWithParams(url, params);
//        }

        this.get(url, listener);
    }

    public void post(String url, final CallBackListener<String> listener, Map<String, String> params,boolean isNeedCommonParams) {
        if(isNeedCommonParams){
            //Methods.setCommonParams(params);
        }

//        int flag = Methods.getUrlEnt(url);
//        if (flag == 2) {
//            // 接入AOS系统，对登录信息进行XXTea加密
//            params = XXTeaHelper.getInstance().encryptAndEncodeToPost(params, false);
//        } else if (flag == 1) {
//            // 登录以外的接口进行AES加密
//            params = AESHelper.getInstance().encryptAndHexToPost(params);
//        } else {
//            // 不加密
//        }

        stringRequest = new BaseStringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (listener != null) {
                            listener.onSuccessResponse(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (listener != null) {
                            listener.onErrorResponse(error);
                        }
                    }
                }
        );
        addRequest(stringRequest, params);
    }

    public void post(String url, final CallBackListener<String> listener, Map<String, String> params) {
        post(url,listener,params,true);
    }

    public void cancelRequest(){
        if(stringRequest != null && !stringRequest.isCanceled()){
            //GDLog.v("suggestresult","cancel");
            stringRequest.cancel();
        }
    }

}

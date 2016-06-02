package com.yjy.netmodule.requestutil;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import com.yjy.netmodule.GxdVolley;
import com.yjy.netmodule.request.BaseStringRequest;

public class BaseRequestUtil<T> {

    private static final String TAG = BaseRequestUtil.class.toString();
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    protected static RequestQueue requestQueue;

    protected BaseRequestUtil(Context context){
        requestQueue = GxdVolley.getInstance(context);
    }

    protected void addRequest(Request request){
        requestQueue.add(request);
    }

    protected void addRequest(BaseStringRequest request){
        this.addRequest(request,null);
    }

    protected void addRequest(BaseStringRequest request,Map<String,String> params){
        request.setmParams(params);
        requestQueue.add(request);
    }

    protected String createGetUrlWithParams(String url,Map<String,String> params){
        if(params != null && url != null){

            StringBuilder stringBuilder = new StringBuilder(url);
            if(!url.contains("?")){
                stringBuilder.append("?");
            }
            for(Map.Entry<String,String> entry : params.entrySet()){

                String key = entry.getKey().toString();
                String value = null;
                if(entry.getValue() == null){
                    value = "";
                }else{
                    value = entry.getValue().toString();
                }
                stringBuilder.append(key);
                stringBuilder.append("=");

                try {
                    value = URLEncoder.encode(value,DEFAULT_PARAMS_ENCODING);
                    stringBuilder.append(value);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                stringBuilder.append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            url = stringBuilder.toString();
        }
        //GDLog.v(TAG,"getURL:" + url);
        return url;
    }




}

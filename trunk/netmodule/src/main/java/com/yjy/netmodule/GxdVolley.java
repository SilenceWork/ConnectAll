package com.yjy.netmodule;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class GxdVolley {
    private static RequestQueue requestQueue;
    public static RequestQueue getInstance(Context context){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
}

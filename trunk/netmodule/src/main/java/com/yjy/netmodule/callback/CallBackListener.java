package com.yjy.netmodule.callback;

import com.android.volley.VolleyError;

public interface CallBackListener<T> {
    void onSuccessResponse(T response);
    void onErrorResponse(VolleyError error);
}

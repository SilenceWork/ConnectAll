package com.yjy.netmodule;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.yjy.netmodule.callback.CallBackListener;
import com.yjy.netmodule.requestutil.StringRequestUtil;

import java.util.HashMap;

import autonavi.com.netmodule.R;

/**
 * Created by zhulimin.zlm on 2016/5/31.
 *
 */
public class NetTestActivity  extends Activity {
    private StringRequestUtil requestUtil;
    public static String CP_TEST_002 = "http://140.205.155.250/real";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.net_module_test_activity);
        requestUtil = new StringRequestUtil(this);

        findViewById(R.id.testVolleyGet).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(NetTestActivity.this, "test volley get", Toast.LENGTH_LONG).show();
                        String url = CP_TEST_002 + "/daolu/road_num";
                        HashMap<String, String> params = new HashMap<String, String>();

                        params.put("version", "Android_3.4.7");
                        params.put("verify", "-1274509978_d3d0db5d2bb86072fbaff43d4867a891_668bcc7ad609c63bb36db301b41be658");
                        params.put("imei", "869043023817182");
                        params.put("sign", "14324|||||");
                        params.put("rt", "json");
                        params.put("phone", "Android5.0.2 21 Xiaomi Redmi Note 2");
                        params.put("from", "gxd");
                        params.put("login_name", "漂泊的朱");
                        params.put("tbid", "2518983813");

                        requestUtil.get(url, new CallBackListener<String>() {
                            @Override
                            public void onSuccessResponse(String response) {
                                Toast.makeText(NetTestActivity.this, "test volley success", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NetTestActivity.this, "test volley failed", Toast.LENGTH_LONG).show();
                            }
                        }, params);
                    }
                });

        findViewById(R.id.testVolleyPost).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

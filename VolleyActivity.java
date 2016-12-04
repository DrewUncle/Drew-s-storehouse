package com.jredu.helloworld.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jredu.helloworld.R;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyActivity extends AppCompatActivity {
    WebView webView;
    Button button;
    Button imgButton;
    ImageView img;
    RequestQueue queue = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        queue = Volley.newRequestQueue(this);
        webView = (WebView) findViewById(R.id.volleyWebView);
        img = (ImageView) findViewById(R.id.img);
        button = (Button) findViewById(R.id.volleyButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //doStringVolley2();
                doStringVolley();
            }
        });
        imgButton = (Button) findViewById(R.id.imgButton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetImg();
            }
        });
    }
    /*get方法*/
    public void doStringVolley(){
        /*创建请求队列*/
        //RequestQueue queue = Volley.newRequestQueue(this);
        /*创建请求对象*/
        StringRequest request = new StringRequest(
                Request.Method.GET,
                /*"http://op.juhe.cn/onebox/basketball/nba?key=8c8405cea6805082a92affcb8b1f1cb0",*/
                "http://apis.baidu.com/txapi/tiyu/tiyu?num=10&page=1&word=%E6%9E%97%E4%B8%B9",
                /*"http://www.baidu.com",*/
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String s = response;
                        Toast.makeText(getApplication(),"网络请求成功！！！",Toast.LENGTH_SHORT).show();
                        webView.getSettings().setDefaultTextEncodingName("utf-8");
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadDataWithBaseURL(null,s,"text/html","utf-8",null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(),"网咯请求失败！！！",Toast.LENGTH_SHORT).show();
                        webView.loadDataWithBaseURL(null,"网络连接失败!!!","text/html","utf-8",null);
                    }
                }
        ){
            //设置请求头
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("apikey","fc642e216cd19906f642ee930ce28174");
                return map;
            }
            /*解析网络请求结果的方法*/
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonObject = new String(new String(response.data, "UTF-8"));
                    return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception je) {
                    return Response.error(new ParseError(je));
                }
            }
            /*设置当前请求的优先级*/
            @Override
            public Priority getPriority() {
                return Priority.LOW;
            }
        };
        request.setTag("get");
        /*把请求对象放入请求队列*/
        queue.add(request);
    }
    /*post方法*/
    public void doStringVolley2(){
        /*创建请求队列*/
        //RequestQueue queue = Volley.newRequestQueue(this);
        /*创建请求对象*/
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://www.baidu.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String s = response;
                        webView.getSettings().setDefaultTextEncodingName("utf-8");
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.loadDataWithBaseURL(null,s,"text/html","utf-8",null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        webView.loadDataWithBaseURL(null,"网络连接失败!!!","text/html","utf-8",null);
                    }
                }
        ){
            /*重写params方法写参数*/
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String, String>();
                map.put("num","10");
                map.put("page","1");
                map.put("word","%E6%9E%97%E4%B8%B9");
                return map;
            }
            /*设置请求对象优先级*/
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }
        };
        request.setTag("post");
        /*把请求对象放入请求队列*/
        queue.add(request);
    }
    /*获取图片*/
    public void GetImg(){
        ImageRequest request = new ImageRequest(
                "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                },
                5000,
                5000,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        img.setImageResource(R.drawable.first5);
                    }
                }
        );
        queue.add(request);
    }
    /*重写onStop方法，用来注销请求*/
    @Override
    protected void onStop() {
        super.onStop();
        /*取消当前请求队列的所有请求*/
        queue.cancelAll(this);
        /*取消当前请求队列tag为get的请求*/
        queue.cancelAll("get");
        /*取消当前请求队列tag为post的请求*/
        queue.cancelAll("post");
    }
}

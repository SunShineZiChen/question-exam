package com.example.wuhe.myapplication;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wuhe.myapplication.bean.QuestionInfo;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.sunshine.basenetwork.HttpClientTManager;
import cn.sunshine.basenetwork.NetWorkRequest;
import cn.sunshine.basenetwork.callback.APIException;
import cn.sunshine.basenetwork.callback.APIResult;
import cn.sunshine.basenetwork.callback.HttpCallback;
import cn.sunshine.basenetwork.callback.HttpCallbackT;
import cn.sunshine.basenetwork.config.NetWorkConfig;

public class MainActivity extends AppCompatActivity implements ChildInterface, View.OnClickListener {
    Button button1, button2, btAnswerActivity;
    TextView tvValue1, tvValue2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        NetWorkConfig.getInstance().setAPIUrl(BuildConfig.CONFIG_API_HOST);
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.bt_request_1);
        button2 = (Button) findViewById(R.id.bt_request_2);
        btAnswerActivity = (Button) findViewById(R.id.bt_AnswerActivity);
        tvValue1 = (TextView) findViewById(R.id.tv_value1);
        tvValue2 = (TextView) findViewById(R.id.tv_value2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        btAnswerActivity.setOnClickListener(this);
    }

    @Override
    public void showMain() {

    }

    @Override
    public void showLogin() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_request_1:
                request1();
                break;
            case R.id.bt_request_2:
                request2();
                break;
            case R.id.bt_AnswerActivity:
                //去做试题
                Intent intent = new Intent(this, QuestionActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void request1() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("city", "成都");
        NetWorkRequest.GET("/weatherApi", map, new HttpCallback<APIResult>() {
            @Override
            public void onSuccess(APIResult data) throws Exception {
                Log.d("=======", "onSuccess");
                Log.d("=======", data.getBody().toString());
                tvValue1.setText(data.getBody().toString());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.d("=======", "onError" + e.getMessage());
            }

            @Override
            public void onComplete() {
                super.onComplete();
                Log.d("=======", "onComplete");
            }
        });
    }

    private void request2() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("city", "成都");
        HttpClientTManager.getInstance().getRetrofit().create(AppApiService.class).executeTGet("/weatherApi", map).enqueue(new HttpCallbackT<TianQiBean>() {
            @Override
            public void onSuccess(TianQiBean data) throws Exception {
                Gson gson = new Gson();
                String string = gson.toJson(data, TianQiBean.class);
                Log.d("=GET_T======", "onSuccess" + string);
                Log.d("=GET_T======", "onSuccess " + data.getMsg());
                Log.d("=GET_T======", "onSuccess " + data.getCode());
                tvValue2.setText(string);
            }

            @Override
            public void onError(APIException e) {
                Log.d("=GET_T======", "onError");
            }
        });
    }
}

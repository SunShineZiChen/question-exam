package com.example.wuhe.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.mylibrary.paper.view.QuestionViewPager;
import com.example.mylibrary.paper.view.SlidingUpPanelLayout;
import com.example.wuhe.myapplication.adapter.TopicAdapter;
import com.example.wuhe.myapplication.bean.QuestionInfo;
import com.example.wuhe.myapplication.fragment.QuestionFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements QuestionFragment.OnModifyQuestionListener,View.OnClickListener{

    private SlidingUpPanelLayout mLayout;
    private TopicAdapter topicAdapter;
    private RecyclerView recyclerView;
    private ImageView shadowView;
    private QuestionViewPager questionViewPager;
    private List<QuestionInfo.Data.DataBean> dataBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quesiton);


        initSlidingUoPanel();

        initList();

        QuestionInfo questionInfo = getQuestions();

        dataBeans = questionInfo.getData().getData();
        Log.i("data.size=", "" + dataBeans.size());

        if (topicAdapter != null) {
            topicAdapter.setDataNum(dataBeans.size());
            topicAdapter.setDatas(dataBeans);
        }

        initReadViewPager();
        findViewById(R.id.bt_pre).setOnClickListener(this);
        findViewById(R.id.bt_next).setOnClickListener(this);
        TextView tvNumbers = (TextView) findViewById(R.id.tv_numbers);
        tvNumbers.setText("0/"+dataBeans.size());

    }

    private int prePosition2;
    private int curPosition2;

    private void initReadViewPager() {
        shadowView = (ImageView) findViewById(R.id.shadowView);
        questionViewPager = (QuestionViewPager) findViewById(R.id.readerViewPager);
        questionViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                QuestionInfo.Data.DataBean subDataBean = dataBeans.get(position);
                QuestionFragment fragment = QuestionFragment.newInstance(subDataBean, position);
                fragment.setModifyQuestionListener(QuestionActivity.this);
                return fragment;
            }

            @Override
            public int getCount() {
                return dataBeans.size();
            }
        });
        questionViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                shadowView.setTranslationX(questionViewPager.getWidth() - positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                curPosition2 = position;
                topicAdapter.notifyCurPosition(curPosition2);
                topicAdapter.notifyPrePosition(prePosition2);

                prePosition2 = curPosition2;
                MoveToPosition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private int prePosition;
    private int curPosition;
    GridLayoutManager gridLayoutManager;
    private void initList() {
        recyclerView = (RecyclerView) findViewById(R.id.list);

         gridLayoutManager = new GridLayoutManager(this, 6);

        topicAdapter = new TopicAdapter(this);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(topicAdapter);


        topicAdapter.setOnTopicClickListener(new TopicAdapter.OnTopicClickListener() {
            @Override
            public void onClick(TopicAdapter.TopicViewHolder holder, int position) {
                curPosition = position;
                if (mLayout != null &&
                        (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                    mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }

                questionViewPager.setCurrentItem(position);

                topicAdapter.notifyCurPosition(curPosition);
                topicAdapter.notifyPrePosition(prePosition);

                prePosition = curPosition;
            }
        });


    }

    private void initSlidingUoPanel() {
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        int height = getWindowManager().getDefaultDisplay().getHeight();

        LinearLayout dragView = (LinearLayout) findViewById(R.id.dragView);
        SlidingUpPanelLayout.LayoutParams params = new SlidingUpPanelLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (height * 0.8f));
        dragView.setLayoutParams(params);


        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
               if(newState==SlidingUpPanelLayout.PanelState.COLLAPSED){
                   MoveToPosition();
               }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    /**
     * 滚动 recycleView 到当前选择的位置
     */
    private void MoveToPosition(){
        gridLayoutManager.scrollToPositionWithOffset(curPosition2, 0);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    public String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    private QuestionInfo getQuestions() {

        try {
            InputStream in = getAssets().open("question.json");
            QuestionInfo questionInfo = JSON.parseObject(inputStream2String(in), QuestionInfo.class);

            return questionInfo;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("data.size=", e.toString());
        }

        return null;
    }

    /**
     * 上一题
     */
    private synchronized void preQuestion(){
        int currentItem = questionViewPager.getCurrentItem();
        currentItem = currentItem - 1;
        if (currentItem > dataBeans.size() - 1) {
            currentItem = dataBeans.size() - 1;
        }
        questionViewPager.setCurrentItem(currentItem);
    }

    /**
     * 下一题
     */
    private synchronized void nextQuestion(){
        int currentItem = questionViewPager.getCurrentItem();
        currentItem = currentItem + 1;
        if (currentItem < 0) {
            currentItem = 0;
        }
        questionViewPager.setCurrentItem(currentItem);
    }

    @Override
    public void modifyQuestion(int selectId, int position) {
        QuestionInfo.Data.DataBean dataBeanTemp = dataBeans.get(position);
        dataBeanTemp.setQuestion_select(selectId);
        nextQuestion();
    }

    @Override
    public synchronized void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_pre:
                preQuestion();
                break;
            case R.id.bt_next:
                nextQuestion();
                break;
        }
    }
}

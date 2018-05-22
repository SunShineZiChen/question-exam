package com.example.wuhe.myapplication.fragment;


import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mylibrary.paper.utils.DensityUtils;
import com.example.wuhe.myapplication.R;
import com.example.wuhe.myapplication.bean.QuestionInfo;


/**
 */
public class QuestionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private QuestionInfo.Data.DataBean subDataBean;
    private int position;
    private View view;
    private OnModifyQuestionListener modifyQuestionListener;

    public OnModifyQuestionListener getModifyQuestionListener() {
        return modifyQuestionListener;
    }

    public void setModifyQuestionListener(OnModifyQuestionListener modifyQuestionListener) {
        this.modifyQuestionListener = modifyQuestionListener;
    }

    public interface OnModifyQuestionListener {
        void modifyQuestion(int answerSelect, int position);
    }

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(QuestionInfo.Data.DataBean subDataBean, int position) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, subDataBean);
        args.putSerializable(ARG_PARAM2, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subDataBean = (QuestionInfo.Data.DataBean) getArguments().getSerializable(ARG_PARAM1);
            position = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question, container, false);
        initView();
        return view;
    }

    private void initView() {
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        String tag=QuestionInfo.getQuestionTypeStr(subDataBean.getOption_type());
        CharSequence content=tag+"  "+(position + 1) + ". " + subDataBean.getQuestion();
        SpannableStringBuilder builder = new SpannableStringBuilder(content);
        builder.setSpan(new ImageSpan(getActivity(),R.drawable.text_background){
            @Override
            public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
                paint.setColor(Color.BLUE);
                paint.setTypeface(Typeface.create("normal", Typeface.NORMAL));
                paint.setTextSize(DensityUtils.sp2px(getContext(), 11));
                canvas.drawText(text.subSequence(start, end).toString(), x+DensityUtils.dp2px(getContext(), 5), y-DensityUtils.dp2px(getContext(), 3), paint);
                super.draw(canvas, text, start, end, x, top, y, bottom-DensityUtils.dp2px(getContext(), 2), paint);
            }
        }, 0, tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
        if (subDataBean.getOption_type() == QuestionInfo.TYPE_Multiple_Choice) {//多想选择题
            updateCheckBoxView();
        } else {
            updateRadioView();
        }
    }


    /**
     * 添加多选按钮
     */
    private void updateCheckBoxView() {
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.check_options);
        for (int i = 0; i < subDataBean.getOptions().size(); i++) {
            QuestionInfo.Data.DataBean.Option option = subDataBean.getOptions().get(i);
            CheckBox checkboxView = (CheckBox) LayoutInflater.from(getActivity()).inflate(R.layout.item_checkbox, null);
            checkboxView.setText(option.getContent());
            checkboxView.setTag(i);
            if (subDataBean.getQuestion_select() == i) {
                checkboxView.setChecked(true);
            } else {
                checkboxView.setChecked(false);
            }
            checkboxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        int op= (int) buttonView.getTag();
                        modifyQuestionListener.modifyQuestion(op, position);
                    }else{

                    }
                }
            });

            layout.addView(checkboxView);
        }
    }
    /**
     * 添加单选按钮
     */
    private void updateRadioView() {
        final RadioGroup layout = (RadioGroup) view.findViewById(R.id.rg_options);
        layout.removeAllViews();
        if(position==0){
            Log.d("这个是数据", ""+subDataBean.getQuestion_select());
        }
        int checkId = -1;
        for (int i = 0; i < subDataBean.getOptions().size(); i++) {
            QuestionInfo.Data.DataBean.Option option = subDataBean.getOptions().get(i);
            final RadioButton radioView = (RadioButton) LayoutInflater.from(getActivity()).inflate(R.layout.item_radio, null, true);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radioView.setLayoutParams(layoutParams);
            radioView.setText(option.getContent());
            radioView.setTag(i);
            radioView.setId(i);
            if (subDataBean.getQuestion_select() == i) {
                checkId=i;
            }
            radioView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        int op= (int) buttonView.getTag();
                        modifyQuestionListener.modifyQuestion(op, position);
                    }
                }
            });
            layout.addView(radioView);
        }
        if(checkId!=-1){
            layout.check(checkId);
        }
    }

}

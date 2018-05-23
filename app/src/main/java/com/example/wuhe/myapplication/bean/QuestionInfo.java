package com.example.wuhe.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangyipeng on 16/6/30.
 */
public class QuestionInfo extends BaseInfo {
    /**
     * 单项选择题
     */
    public static final int TYPE_Single_Choice = 1;
    /**
     * 判断题
     */
    public static final int TYPE_True_OR_False = 2;
    /**
     * 多项选择题
     */
    public static final int TYPE_Multiple_Choice = 3;
    public static String getQuestionTypeStr(int type){
        switch (type){
            case TYPE_Single_Choice:
                return "单选题";
            case TYPE_True_OR_False:
                return "判断题";
            case TYPE_Multiple_Choice:
                return "多选题";
        }
        return "单选题";
    }
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        private List<DataBean> data;

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public List<DataBean> getData() {
            return data;
        }


        public static class DataBean implements Serializable {
            private int answer;
            private String explain;
            private List<Option> options;
            private String question;
            private int question_id;
            private int media_type;
            private String media_content;
            private int option_type;
            private int question_select;

            public void setAnswer(int answer) {
                this.answer = answer;
            }

            public int getAnswer() {
                return answer;
            }

            public void setExplain(String explain) {
                this.explain = explain;
            }

            public String getExplain() {
                return explain;
            }

            public void setOptions(List<Option> options) {
                this.options = options;
            }

            public List<Option> getOptions() {
                return options;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion_id(int question_id) {
                this.question_id = question_id;
            }

            public int getQuestion_id() {
                return question_id;
            }

            public void setMedia_type(int media_type) {
                this.media_type = media_type;
            }

            public int getMedia_type() {
                return media_type;
            }

            public void setMedia_content(String media_content) {
                this.media_content = media_content;
            }

            public String getMedia_content() {
                return media_content;
            }

            public void setOption_type(int option_type) {
                this.option_type = option_type;
            }

            public int getOption_type() {
                return option_type;
            }

            public int getQuestion_select() {
                return question_select;
            }

            public void setQuestion_select(int question_select) {
                this.question_select = question_select;
            }

            public static class Option implements Serializable {
                private String type;
                private String content;
                private boolean is_correct;

                public void setType(String type) {
                    this.type = type;
                }

                public String getType() {
                    return type;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getContent() {
                    return content;
                }

                public void setIs_correct(boolean is_correct) {
                    this.is_correct = is_correct;
                }

                public boolean getIs_correct() {
                    return is_correct;
                }

            }
        }
    }
}

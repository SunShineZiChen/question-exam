package com.example.wuhe.myapplication;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wuhe on 2018/3/8.
 */

public class TianQiBean implements Serializable{

    private int code;
    private String msg;
    private Data data;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }
    /**
     * Auto-generated: 2018-03-19 11:17:19
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Yesterday {

        private String date;
        private String high;
        private String fx;
        private String low;
        private String fl;
        private String type;
        public void setDate(String date) {
            this.date = date;
        }
        public String getDate() {
            return date;
        }

        public void setHigh(String high) {
            this.high = high;
        }
        public String getHigh() {
            return high;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }
        public String getFx() {
            return fx;
        }

        public void setLow(String low) {
            this.low = low;
        }
        public String getLow() {
            return low;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }
        public String getFl() {
            return fl;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

    }

    /**
     * Auto-generated: 2018-03-19 11:17:19
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Forecast {

        private String date;
        private String high;
        private String fengli;
        private String low;
        private String fengxiang;
        private String type;
        public void setDate(String date) {
            this.date = date;
        }
        public String getDate() {
            return date;
        }

        public void setHigh(String high) {
            this.high = high;
        }
        public String getHigh() {
            return high;
        }

        public void setFengli(String fengli) {
            this.fengli = fengli;
        }
        public String getFengli() {
            return fengli;
        }

        public void setLow(String low) {
            this.low = low;
        }
        public String getLow() {
            return low;
        }

        public void setFengxiang(String fengxiang) {
            this.fengxiang = fengxiang;
        }
        public String getFengxiang() {
            return fengxiang;
        }

        public void setType(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }

    }
    /**
     * Auto-generated: 2018-03-19 11:17:19
     *
     * @author bejson.com (i@bejson.com)
     * @website http://www.bejson.com/java2pojo/
     */
    public class Data {

        private Yesterday yesterday;
        private String city;
        private String aqi;
        private List<Forecast> forecast;
        private String ganmao;
        private String wendu;
        public void setYesterday(Yesterday yesterday) {
            this.yesterday = yesterday;
        }
        public Yesterday getYesterday() {
            return yesterday;
        }

        public void setCity(String city) {
            this.city = city;
        }
        public String getCity() {
            return city;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }
        public String getAqi() {
            return aqi;
        }

        public void setForecast(List<Forecast> forecast) {
            this.forecast = forecast;
        }
        public List<Forecast> getForecast() {
            return forecast;
        }

        public void setGanmao(String ganmao) {
            this.ganmao = ganmao;
        }
        public String getGanmao() {
            return ganmao;
        }

        public void setWendu(String wendu) {
            this.wendu = wendu;
        }
        public String getWendu() {
            return wendu;
        }

    }

    public static void logJson(String json) throws JSONException {
        JSONObject jsonObject=new JSONObject(json);
        Iterator iterator = jsonObject.keys();
        while(iterator.hasNext()){
            String key = (String) iterator.next();
            Object obj=jsonObject.get(key);
            if(obj instanceof JSONArray){

            }
            String value = jsonObject.getString(key);
        }
    }
}

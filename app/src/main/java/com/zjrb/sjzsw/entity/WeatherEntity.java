package com.zjrb.sjzsw.entity;

import java.util.List;

public class WeatherEntity {

    /**
     * shidu : 53%
     * pm25 : 64
     * pm10 : 107
     * quality : 轻度污染
     * wendu : 29
     * ganmao : 儿童、老年人及心脏、呼吸系统疾病患者人群应减少长时间或高强度户外锻炼
     * yesterday : {"date":"04日星期三","sunrise":"04:50","high":"高温 36.0℃","low":"低温 25.0℃","sunset":"19:46","aqi":124,"fx":"西南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"}
     * forecast : [{"date":"05日星期四","sunrise":"04:51","high":"高温 37.0℃","low":"低温 24.0℃","sunset":"19:46","aqi":75,"fx":"东南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"06日星期五","sunrise":"04:51","high":"高温 33.0℃","low":"低温 23.0℃","sunset":"19:46","aqi":69,"fx":"东南风","fl":"<3级","type":"雷阵雨","notice":"带好雨具，别在树下躲雨"},{"date":"07日星期六","sunrise":"04:52","high":"高温 31.0℃","low":"低温 21.0℃","sunset":"19:46","aqi":59,"fx":"东南风","fl":"3-4级","type":"雷阵雨","notice":"带好雨具，别在树下躲雨"},{"date":"08日星期日","sunrise":"04:52","high":"高温 27.0℃","low":"低温 20.0℃","sunset":"19:45","aqi":58,"fx":"东北风","fl":"<3级","type":"雷阵雨","notice":"带好雨具，别在树下躲雨"},{"date":"09日星期一","sunrise":"04:53","high":"高温 29.0℃","low":"低温 21.0℃","sunset":"19:45","aqi":62,"fx":"东南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"}]
     */

    private String shidu;
    private int pm25;
    private int pm10;
    private String quality;
    private String wendu;
    private String ganmao;
    private YesterdayBean yesterday;
    private List<ForecastBean> forecast;

    public String getShidu() {
        return shidu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public int getPm25() {
        return pm25;
    }

    public void setPm25(int pm25) {
        this.pm25 = pm25;
    }

    public int getPm10() {
        return pm10;
    }

    public void setPm10(int pm10) {
        this.pm10 = pm10;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getWendu() {
        return wendu;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public String getGanmao() {
        return ganmao;
    }

    public void setGanmao(String ganmao) {
        this.ganmao = ganmao;
    }

    public YesterdayBean getYesterday() {
        return yesterday;
    }

    public void setYesterday(YesterdayBean yesterday) {
        this.yesterday = yesterday;
    }

    public List<ForecastBean> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastBean> forecast) {
        this.forecast = forecast;
    }

    public static class YesterdayBean {
        /**
         * date : 04日星期三
         * sunrise : 04:50
         * high : 高温 36.0℃
         * low : 低温 25.0℃
         * sunset : 19:46
         * aqi : 124
         * fx : 西南风
         * fl : <3级
         * type : 多云
         * notice : 阴晴之间，谨防紫外线侵扰
         */

        private String date;
        private String sunrise;
        private String high;
        private String low;
        private String sunset;
        private int aqi;
        private String fx;
        private String fl;
        private String type;
        private String notice;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        @Override
        public String toString() {
            return "YesterdayBean{" +
                    "date='" + date + '\'' +
                    ", sunrise='" + sunrise + '\'' +
                    ", high='" + high + '\'' +
                    ", low='" + low + '\'' +
                    ", sunset='" + sunset + '\'' +
                    ", aqi=" + aqi +
                    ", fx='" + fx + '\'' +
                    ", fl='" + fl + '\'' +
                    ", type='" + type + '\'' +
                    ", notice='" + notice + '\'' +
                    '}';
        }
    }

    public static class ForecastBean {
        /**
         * date : 05日星期四
         * sunrise : 04:51
         * high : 高温 37.0℃
         * low : 低温 24.0℃
         * sunset : 19:46
         * aqi : 75
         * fx : 东南风
         * fl : <3级
         * type : 多云
         * notice : 阴晴之间，谨防紫外线侵扰
         */

        private String date;
        private String sunrise;
        private String high;
        private String low;
        private String sunset;
        private int aqi;
        private String fx;
        private String fl;
        private String type;
        private String notice;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public String getFx() {
            return fx;
        }

        public void setFx(String fx) {
            this.fx = fx;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        @Override
        public String toString() {
            return "ForecastBean{" +
                    "date='" + date + '\'' +
                    ", sunrise='" + sunrise + '\'' +
                    ", high='" + high + '\'' +
                    ", low='" + low + '\'' +
                    ", sunset='" + sunset + '\'' +
                    ", aqi=" + aqi +
                    ", fx='" + fx + '\'' +
                    ", fl='" + fl + '\'' +
                    ", type='" + type + '\'' +
                    ", notice='" + notice + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "shidu='" + shidu + '\'' +
                ", pm25=" + pm25 +
                ", pm10=" + pm10 +
                ", quality='" + quality + '\'' +
                ", wendu='" + wendu + '\'' +
                ", ganmao='" + ganmao + '\'' +
                ", yesterday=" + yesterday +
                ", forecast=" + forecast +
                '}';
    }
}

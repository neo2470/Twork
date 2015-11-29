package com.alex.entity;

import android.content.Context;

import com.alex.twork.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alex on 15年9月21日.
 * 新闻类
 */
public class News {

    public News(JSONObject data) {
        nid = data.optString("nid");
        title = data.optString("title").replace(" ", "");
        desc = data.optString("desc");
        link = data.optString("link");

        // 解析新闻配图
        final String imageUrls = "imageurls";
        if (data.has(imageUrls)) {
            final JSONArray array = data.optJSONArray(imageUrls);
            if (1 <= array.length()) {
                final JSONObject obj = array.optJSONObject(0);
                imgUrl = obj.optString("url");
            } else {
                imgUrl = null;
            }
        } else {
            imgUrl = null;
        }

        source = data.optString("source");
        pubDate = data.optString("pubDate");
    }

    public String getNid() {
        return nid;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLink() {
        return link;
    }

    public boolean hasImage() {
        return null != imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSource() {
        return source;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getPubDateFromNow(Context context) {
        // TODO here has BUG

        String result = "";
        final String dateWithTime = pubDate;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {

            final long now = new Date().getTime() / 1000;
            final long date = df.parse(dateWithTime).getTime() / 1000;

            long differ = now - date;

            if(60 > differ) {
                result = differ + " " + context.getString(R.string.news_time_second);
            } else if(3600 > differ) {
                differ /= 60;
                result = differ + " " + context.getString(R.string.news_time_minute);
            } else if(86400 > differ) {
                differ /= 3600;
                result = differ + " " + context.getString(R.string.news_time_hour);
            } else {
                result = dateWithTime.split(" ")[0];
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean isAppeared() {
        return appeared;
    }

    public void setAppeared(boolean appeared) {
        this.appeared = appeared;
    }

    @Override
    public boolean equals(Object news) {
        return news instanceof News && nid.equals(((News) news).getNid());
    }

    private String nid;// 新闻ID
    private String title;// 新闻标题
    private String desc;// 新闻描述
    private String link;// 具体内容链接
    private String imgUrl;// 新闻配图
    private String source;// 新闻来源
    private String pubDate;// 发布时间
    private boolean appeared;// 是否已呈现
}

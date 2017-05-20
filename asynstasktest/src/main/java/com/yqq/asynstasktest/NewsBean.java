package com.yqq.asynstasktest;

/**
 * Created by user on 2017/4/28.
 */

public class NewsBean {
    private String newsIconUrl;
    private String newsTitle;
    private String newsContent;

    public void setNewsIconUrl(String newsIconUrl) {
        this.newsIconUrl = newsIconUrl;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsIconUrl() {

        return newsIconUrl;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsContent() {
        return newsContent;
    }
}

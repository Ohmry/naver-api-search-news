package io.github.ohmry.naver.api.search.news;

import java.time.LocalDateTime;

public class NaverNews {
    public final String title;
    public final String originalink;
    public final String link;
    public final String description;
    public final LocalDateTime pubDate;

    public NaverNews(String title, String originalink, String link, String description, LocalDateTime pubDate) {
        this.title = title;
        this.originalink = originalink;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("title:" + title + ",");
        stringBuilder.append("originalink:" + originalink + ",");
        stringBuilder.append("link:" + link + ",");
        stringBuilder.append("description:" + description + ",");
        stringBuilder.append("pubDate:" + pubDate + ",");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

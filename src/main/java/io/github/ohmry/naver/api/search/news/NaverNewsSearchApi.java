package io.github.ohmry.naver.api.search.news;

import io.github.ohmry.utils.Curl;
import io.github.ohmry.utils.JsonUtils;
import io.github.ohmry.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 네이버 뉴스 검색 API
 */
public class NaverNewsSearchApi {
    private final String BASE_URL = "https://openapi.naver.com/v1/search/news.json";
    private final String naverClientId;
    private final String naverClientSecret;
    private String query;
    private int display;
    private int start;
    private NaverNewsSearchSortType sort;

    public NaverNewsSearchApi(String naverClientId, String naverClientSecret) {
        this.naverClientId = naverClientId;
        this.naverClientSecret = naverClientSecret;
        this.display = 10;
        this.start = 1;
        this.sort = NaverNewsSearchSortType.ACCURACY;
    }

    public void setQuery(String query) {
        try {
            this.query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("query 값의 형식이 올바르지 않습니다.");
        }
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setSort(NaverNewsSearchSortType sort) {
        this.sort = sort;
    }

    public NaverNewsSearchResult fetch() {
        if (!StringUtils.hasText(this.query)) {
            throw new NullPointerException("query 값은 비어있을 수 없습니다.");
        }
        if (this.display < 1 || this.display > 100) {
            throw new IllegalArgumentException("display 값은 1 ~ 100 사이의 값으로 설정할 수 있습니다.");
        }
        if (this.start < 1 || this.start > 100) {
            throw new IllegalArgumentException("start 값은 1 ~ 100 사이의 값으로 설정할 수 있습니다.");
        }
        if (this.sort == null) {
            throw new NullPointerException("sort 값은 비어있을 수 없습니다.");
        }

        String queryUrl = this.BASE_URL + "?query=" + this.query + "&display=" + this.display + "&start=" + this.start + "&sort=" + this.sort.getCode();
        Curl curl = new Curl(queryUrl);
        curl.setMethod("GET");
        curl.setHeader("X-Naver-Client-Id", this.naverClientId);
        curl.setHeader("X-Naver-Client-Secret", this.naverClientSecret);
        String response = curl.fetch();

        boolean isSuccess = curl.isSuccess();
        NaverNewsSearchResult result = new NaverNewsSearchResult();
        result.setSuccess(isSuccess);
        if (isSuccess) {
            result.setTotal(JsonUtils.getInteger(response, "total"));
            result.setDisplay(JsonUtils.getInteger(response, "display"));
            result.setStart(JsonUtils.getInteger(response, "start"));
            result.setLastBuildDate(JsonUtils.getLocalDateTime(response, "lastBuildDate", "EEE, d MMM yyyy HH:mm:ss Z"));

            List<NaverNews> naverNewsList = new ArrayList<>();
            List<String> items = JsonUtils.getArray(response, "items");
            for (String item : items) {
                String title = JsonUtils.getString(item, "title");
                String originallink = JsonUtils.getString(item, "originallink");
                String link = JsonUtils.getString(item, "link");
                String description = JsonUtils.getString(item, "description");
                LocalDateTime pubDate = JsonUtils.getLocalDateTime(item, "pubDate", "EEE, d MMM yyyy HH:mm:ss Z");
                NaverNews naverNews = new NaverNews(title, originallink, link, description, pubDate);
                naverNewsList.add(naverNews);
            }
            result.setItems(naverNewsList);
        } else {
            result.setErrorCode(JsonUtils.getString(response, "errorCode"));
            result.setErrorMessage(JsonUtils.getString(response, "errorMessage"));
        }
        return result;
    }
}

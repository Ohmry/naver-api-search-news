package io.github.ohmry.naver.api.search.news;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 네이버 뉴스 검색 API
 */
public class NaverNewsSearchApi {
    private final String BASE_URL = "https://openapi.naver.com/v1/search/news.json";
    private final String CLIENT_ID;
    private final String CLIENT_SECRET;

    public NaverNewsSearchApi(String CLIENT_ID, String CLIENT_SECRET) {
        this.CLIENT_ID = CLIENT_ID;
        this.CLIENT_SECRET = CLIENT_SECRET;
    }

    public NaverNewsSearchApiBuilder builder() {
        return new NaverNewsSearchApiBuilder(this);
    }

    public class NaverNewsSearchApiBuilder {
        private NaverNewsSearchApi api;
        private String query;
        private int display;
        private int start;
        private String sort;

        private NaverNewsSearchApiBuilder() {}
        private NaverNewsSearchApiBuilder(NaverNewsSearchApi api) {
            this.api = api;
            this.display = 10;
            this.start = 1;
            this.sort = NaverNewsSearchApiSortType.ACCURACY;
        }

        public NaverNewsSearchApiBuilder query(String query) {
            try {
                this.query = URLEncoder.encode(query, "UTF-8");
            } catch (Exception ex) {
                ex.printStackTrace();
                this.query = null;
            } finally {
                return this;
            }
        }

        public NaverNewsSearchApiBuilder display(int display) {
            this.display = display;
            return this;
        }

        public NaverNewsSearchApiBuilder start(int start) {
            this.start = start;
            return this;
        }

        public NaverNewsSearchApiBuilder sort(String sort) {
            this.sort = sort;
            return this;
        }

        public List<NaverNews> fetch() {
            try {
                String queryUrl = api.BASE_URL +
                        "?query=" + this.query +
                        "&display=" + this.display +
                        "&start=" + this.start +
                        "&sort" + this.sort;

                URL url = new URL(queryUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Naver-Client-Id", api.CLIENT_ID);
                connection.setRequestProperty("X-Naver-Client-Secret", api.CLIENT_SECRET);

                int responseCode = connection.getResponseCode();
                BufferedReader reader;

                if (responseCode >= 200 && responseCode < 300) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }

                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line.replaceAll("\\t", ""));
                }

                Pattern itemsPattern = Pattern.compile("\\\"items\\\":\\[(\\{.+?\\})\\]");
                Matcher itemsMatcher = itemsPattern.matcher(stringBuilder.toString());

                if (!itemsMatcher.find()) {
                    throw new NaverNewsNotFoundException();
                }

                List<NaverNews> naverNewsList = new ArrayList<>();
                String items = itemsMatcher.group(1);
                Pattern newsPattern = Pattern.compile("\\{.+?\\}");
                Matcher newsMatcher = newsPattern.matcher(items);

                while (newsMatcher.find()) {
                    String news = newsMatcher.group(0);
                    Pattern titlePattern = Pattern.compile("\\\"title\\\":\\\"(.+?)\\\"");
                    Pattern originallinkPattern = Pattern.compile("\\\"originallink\\\":\\\"(.+?)\\\"");
                    Pattern linkPattern = Pattern.compile("\\\"link\\\":\\\"(.+?)\\\"");
                    Pattern descriptionPattern = Pattern.compile("\\\"description\\\":\\\"(.+?)\\\"");
                    Pattern pubDatePattern = Pattern.compile("\\\"pubDate\\\":\\\"(.+?)\\\"");

                    String title;
                    String originallink;
                    String link;
                    String description;
                    LocalDateTime pubDate;

                    Matcher infoMatcher = titlePattern.matcher(news);
                    title = infoMatcher.find() ? infoMatcher.group(1) : null;
                    infoMatcher = originallinkPattern.matcher(news);
                    originallink = infoMatcher.find() ? infoMatcher.group(1) : null;
                    infoMatcher = linkPattern.matcher(news);
                    link = infoMatcher.find() ? infoMatcher.group(1) : null;
                    infoMatcher = descriptionPattern.matcher(news);
                    description = infoMatcher.find() ? infoMatcher.group(1) : null;
                    infoMatcher = pubDatePattern.matcher(news);
                    if (infoMatcher.find()) {
                        pubDate = LocalDateTime.from(DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss Z").parse(infoMatcher.group(1)));
                    } else {
                        pubDate = null;
                    }
                    NaverNews naverNews = new NaverNews(title, originallink, link, description, pubDate);
                    naverNewsList.add(naverNews);
                }
                return naverNewsList;
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ArrayList<>();
            }
        }
    }
}

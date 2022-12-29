package io.github.ohmry.naver.api.search.news;

import org.junit.jupiter.api.Test;

import java.util.List;

public class NaverNewsSearchApiTests {
    @Test
    public void Test() {
        NaverNewsSearchApi naverNewsSearchApi = new NaverNewsSearchApi("", "");
        final int displayCount = 10;
        List<NaverNews> list = naverNewsSearchApi.builder()
                .query("청년도약계좌")
                .display(displayCount)
                .sort(NaverNewsSearchApiSortType.ACCURACY)
                .fetch();

        for (NaverNews news : list) {
            System.out.println("------------------------------------------");
            System.out.println(String.format("%15s: %s", "title", news.title));
            System.out.println(String.format("%15s: %s", "originalink", news.originalink));
            System.out.println(String.format("%15s: %s", "link", news.link));
            System.out.println(String.format("%15s: %s", "description", news.description));
            System.out.println(String.format("%15s: %s", "pubDate", news.pubDate));
        }
        System.out.println("------------------------------------------");
    }
}

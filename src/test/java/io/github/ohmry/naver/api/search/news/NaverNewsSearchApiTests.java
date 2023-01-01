package io.github.ohmry.naver.api.search.news;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class NaverNewsSearchApiTests {
    public static String NAVER_CLIENT_ID;
    public static String NAVER_CLIENT_SECRET;

    @BeforeAll
    static void beforeAll() {
        Map<String, String> environment = System.getenv();
        NaverNewsSearchApiTests.NAVER_CLIENT_ID = environment.get("NAVER_CLIENT_ID");
        NaverNewsSearchApiTests.NAVER_CLIENT_SECRET = environment.get("NAVER_CLIENT_SECRET");
    }

    @Test
    public void AuthorizedFailed() {
        NaverNewsSearchApi naverNewsSearchApi = new NaverNewsSearchApi("", NAVER_CLIENT_SECRET);
        naverNewsSearchApi.setQuery("부동산");
        naverNewsSearchApi.setDisplay(10);
        naverNewsSearchApi.setSort(NaverNewsSearchSortType.DATE);
        naverNewsSearchApi.setStart(2);
        NaverNewsSearchResult result = naverNewsSearchApi.fetch();

        assert result.isSuccess() == false;
        assert result.getItems() == null;
    }

    @Test
    public void Success() {
        NaverNewsSearchApi naverNewsSearchApi = new NaverNewsSearchApi(NAVER_CLIENT_ID, NAVER_CLIENT_SECRET);
        naverNewsSearchApi.setQuery("부동산");
        naverNewsSearchApi.setDisplay(10);
        naverNewsSearchApi.setSort(NaverNewsSearchSortType.DATE);
        naverNewsSearchApi.setStart(2);
        NaverNewsSearchResult result = naverNewsSearchApi.fetch();

        assert result.isSuccess() == true;
        assert result.getItems() != null;

        for (NaverNews naverNews : result.getItems()) {
            System.out.println(String.format("%15s: %s", "title", naverNews.title));
            System.out.println(String.format("%15s: %s", "originallink", naverNews.originalink));
            System.out.println(String.format("%15s: %s", "link", naverNews.link));
            System.out.println(String.format("%15s: %s", "description", naverNews.description));
            System.out.println(String.format("%15s: %s", "pubDate", naverNews.pubDate));
            System.out.println("-----------------------------------------");
        }
    }
}

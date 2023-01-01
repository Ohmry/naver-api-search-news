package io.github.ohmry.naver.api.search.news;

public enum NaverNewsSearchSortType {
    ACCURACY("sim"),
    DATE("date")
    ;

    private String code;

    NaverNewsSearchSortType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}

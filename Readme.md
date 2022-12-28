# 네이버 뉴스 검색 API 모듈
이 프로젝트는 네이버에서 제공하는 Open API 중에서 뉴스를 검색하는 API를 보다 쉽게 사용할 수 있도록 개발한 모듈입니다.

## 사용방법
### 네이버 개발자센터에서 어플리케이션 등록
모듈을 사용하기 위해서는 먼저 [네이버 개발자센터에서 어플리케이션을 등록](https://developers.naver.com/apps/#/register)하고 CLIENT_ID와 CLIENT_SECRET 값이 필요합니다.
이 2개의 값은 모듈을 생성할 때 생성자의 파라미터로 사용되며, 실제 API를 호출할 때 사용되므로 반드시 발급받아야합니다.

### 프로젝트 내 디펜던스(dependency) 추가
모듈은 [https://repo1.maven.org/maven2](https://repo1.maven.org/maven2) 레파지토리에 업로드되어 있으며, 메이븐 프로젝트에서 디펜던시를 추가하여 모듈을 추가할 수 있습니다.
```xml
<dependency>
    <groupId>io.github.ohmry</groupId>
    <artifactId>naver-api-search-news</artifactId>
    <version>0.1</version>
</dependency>
```
메이븐 외 다른 관리툴에 대한 예시를 보려면 [이곳](https://central.sonatype.dev/artifact/io.github.ohmry/naver-api-search-news/0.1)을 클릭하세요.

### 객체 생성 및 API 호출
디펜던시를 추가한 뒤 프로젝트에서 아래와 같이 객체를 생성하고, 사용할 수 있습니다. API를 호출하는 방식은 빌더패턴을 이용하여 각 파라미터에 대한 값을 설정하고, API를 호출할 수 있습니다.

### 객체 생성 및 사용
```java
// 네이버 개발자센터에서 등록한 어플리케이션의 ID와 SECRET값을 사용하여 객체를 생성합니다.
NaverNewsSearchApi naverNewsSearchApi = new NaverNewsSearchAPI(CLIENT_ID, CLIENT_SECRET);

// API 호출
List<NaverNews> newsList = naverNewsSearchApi.builder()
                                             .query("검색어")
                                             .display(10)                                  // 한번 조회할 때, 가져올 데이터의 수 (기본값 10)
                                             .start(1)                                     // 조회할 때, 페이지의 번호 (기본값 1)
                                             .sort(NaverNewsSearchApiSortType.ACCURACY)    // 정렬방식 (ACCURACY: 정확도, DATE: 최신순)
                                             .fetch();

// 응답받은 뉴스 정보를 출력
for (NaverNews naverNews : newsList) {
    System.out.println(String.format("%15s: %s", "title", naverNews.title);                 // 제목
    System.out.println(String.format("%15s: %s", "originallink", naverNews.originallink);   // 실제 뉴스 링크
    System.out.println(String.format("%15s: %s", "link", naverNews.link);                   // 네이버 뉴스 링크
    System.out.println(String.format("%15s: %s", "description", naverNews.description);     // 내용
    System.out.println(String.format("%15s: %s", "pubDate", naverNews.pubDate);             // 발행일시
}
```
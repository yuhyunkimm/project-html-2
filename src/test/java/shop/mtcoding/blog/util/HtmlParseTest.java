package shop.mtcoding.blog.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class HtmlParseTest {
    // 태그와 속성 테스트를 두개 만들어 준다

    @Test
    public void jsoup_test1() throws Exception {
        System.out.println("===============================================");
        Document doc = Jsoup.connect("https://en.wikipedia.org/").get();
        System.out.println(doc.title());
        System.out.println("===============================================");
        Elements newsHeadlines = doc.select("#mp-itn b a");
        for (Element headline : newsHeadlines) {
            System.out.println(headline.attr("title"));
            System.out.println(headline.absUrl("href"));
        }
    }

    @Test
    public void jsoup_test2() {
        String html = "<p>1</p><p><img src=\"data:image/png;base64,iVBORw0KG\"></p>";
        Document doc = Jsoup.parse(html);
        // System.out.println(doc);
        Elements els = doc.select("img"); // elements(복수) 배열로
        // System.out.println(els);
        if (els.size() == 0) {
            // 임시 사진 제공해주기
            // DB thumbnail -> /imaege/profile.jfif
        } else {
            Element el = els.get(0);
            String img = el.attr("src");
            System.out.println(img);
            // DB thumbnail -> img
            // insert / update 할때 코드를 넣어줘야한다
        }
    }

    @Test
    public void parse_test1() {
        String html = "<p>1</p><p><img src=\"data:image/png;base64,iVBORw0KG\"></p>";
        String tag = parseEL(html, "img");
        System.out.println(tag);
        String attr = parseAttr(tag, "src");
        System.out.println(attr);
    }

    private String parseEL(String html, String tag) {
        String s1 = html.substring(html.indexOf(tag) - 1);
        return s1.substring(0, s1.indexOf(">") + 1);
    }

    private String parseAttr(String el, String attr) {
        String s1 = el.substring(el.indexOf(attr));

        int begin = s1.indexOf("\"");
        int end = s1.lastIndexOf("\"");

        return s1.substring(begin + 1, end);
    }

    // given
    String result = "";
    String html = "<p>1</p><p><img src=\"data:image/png;base64,iVBORwOKG\"></P>";

    // when
    // 사진확인
    // boolean isImg = html.contains("img src");
    // if (isImg) {
    // System.out.println("사진있음");
    // } else {
    // System.out.println("사진없음");
    // result = "a.png";
    // }

    // 문자열 위치 찾기
    // int b1 = html.indexOf("img");
    // System.out.println(b1);

    // String s1 = html.substring(b1);
    // System.out.println(s1);

    // int b2 = s1.indexOf("src");
    // String s2 = s1.substring(b2);
    // System.out.println(s2);

    // int b3 = s2.indexOf("\"");
    // int b4 = s2.lastIndexOf("\"");

    // System.out.println(b3);
    // System.out.println(b4);

    // result = s2.substring(b3 + 1, b4);
    // System.out.println(result);
}

package shop.mtcoding.blog.util;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.SingleTestExecutor;

public class HtmlParseTest {
    @Test
    public void parse_test1() {
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
        int b1 = html.indexOf("img");
        System.out.println(b1);

        String s1 = html.substring(b1);
        System.out.println(s1);

        int b2 = s1.indexOf("src");
        String s2 = s1.substring(b2);
        System.out.println(s2);

        int b3 = s2.indexOf("\"");
        int b4 = s2.lastIndexOf("\"");

        System.out.println(b3);
        System.out.println(b4);

        result = s2.substring(b3 + 1, b4);
        System.out.println(result);
    }

}

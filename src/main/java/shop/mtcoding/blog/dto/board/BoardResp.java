package shop.mtcoding.blog.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardResp {

    @Getter
    @Setter
    public static class BoardMainRespDto {
        private int id;
        private String title;
        private String username;

    }

    @Getter
    @Setter
    public static class BoardDetailRespDto {
        private int id;
        private String title;
        private String content;
        private int userId;
        private String username;

    }
}

package shop.mtcoding.blog.dto.board;

import lombok.Getter;
import lombok.Setter;

public class BoardReq {

    @Getter
    @Setter
    public class BoardSaveReqDto {
        private int id;
        private String title;
        private String username;

        public String getContent() {
            return null;
        }
    }

    @Getter
    @Setter
    public class BoardUpdateReqDto {
        private int id;
        private String title;
        private String content;

    }
}

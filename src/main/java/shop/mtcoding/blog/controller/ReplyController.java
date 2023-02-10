package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;

@Controller
public class ReplyController {
    @Autowired
    private HttpSession session;

    // user검증과 관리자가 이용
    @PostMapping("/reply") // where에 들어가지 않으면 주소에 걸지 않는다 => 컨벤션따라 유동적으로 사용
    public String save(ReplySaveReqDto replySaveReqDto) {
        // 댓글1
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        if (replySaveReqDto.getComment() == null || replySaveReqDto.getComment().isEmpty()) {
            throw new CustomApiException("comment 작성해주세요");
        }
        if (replySaveReqDto.getBoardId() == null) {
            throw new CustomApiException("boardId가 필요합니다");
        }
        // 서비스 호출(replySaveReqDto, principal.getId())
        return "redirect:/board/" + replySaveReqDto.getBoardId();
    }

    @Getter
    @Setter
    public static class ReplySaveReqDto {
        private String comment;
        private Integer boardId; // int 대신 사용하면 비어있으면 null 사용 가능
    }
}

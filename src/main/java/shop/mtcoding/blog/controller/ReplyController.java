package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.ReplyService;

@Controller
public class ReplyController {
    @Autowired
    private HttpSession session;

    @Autowired
    private ReplyService replyService;

    // user검증과 관리자가 이용
    @PostMapping("/reply") // where에 들어가지 않으면 주소에 걸지 않는다 => 컨벤션따라 유동적으로 사용
    public String save(ReplySaveReqDto replySaveReqDto) {
        // 댓글1
        System.out.println("테스트 : " + replySaveReqDto.getComment());
        System.out.println("테스트 : " + replySaveReqDto.getBoardId());
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        if (replySaveReqDto.getComment() == null || replySaveReqDto.getComment().isEmpty()) {
            throw new CustomException("comment 작성해주세요");
        }
        if (replySaveReqDto.getBoardId() == null) {
            throw new CustomException("boardId가 필요합니다");
        }
        // 서비스 호출(replySaveReqDto, principal.getId())
        replyService.댓글쓰기(replySaveReqDto, principal.getId());
        return "redirect:/board/" + replySaveReqDto.getBoardId();
    }

}

package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.mtcoding.blog.dto.ResponsDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.service.BoardService;

@Controller
public class BoardController {
    @Autowired
    private HttpSession session;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @DeleteMapping("/board/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable int id) {
        // 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        // DB에서 봐야 권한 검사를 할 수 있다(DB없다)
        // 삭제는 서비스
        boardService.게시글삭제(id, principal.getId());
        // 응답의 dto를 만들어 줘야한다 => ResponsDto
        // $.ajax.done().fail()=>done 200 fail 나머지
        return new ResponseEntity<>(new ResponsDto<>(1, "삭제성공", null), HttpStatus.OK);

    }

    // Status = 401
    @PostMapping("/board")
    public String save(BoardSaveReqDto boadSaveReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        if (boadSaveReqDto.getTitle() == null || boadSaveReqDto.getTitle().isEmpty()) {
            throw new CustomException("Title 작성해주세요");
        }
        if (boadSaveReqDto.getContent() == null || boadSaveReqDto.getContent().isEmpty()) {
            throw new CustomException("Content 작성해주세요");
        }
        if (boadSaveReqDto.getTitle().length() > 100) {
            throw new CustomException("Title의 길이가 100자 이하여야 합니다");
        }
        boardService.글쓰기(boadSaveReqDto, principal.getId());

        return "redirect:/";
    }

    @GetMapping({ "/", "/board" })
    // 조회를 하는것은 서비스로 가지말자
    // C - S - R => 조회는 바로 레파지토리로 가게
    public String main(Model model) {
        model.addAttribute("dtos", boardRepository.findAllWithUser());
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("dto", boardRepository.findByIdWithUser(id));
        return "board/detail";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }

    @PutMapping("/board/{id}/boardUpdateForm")
    public @ResponseBody ResponseEntity<?> update(@PathVariable int id, String title, String content) { // 1. dto 만들어서 =
                                                                                                        // title,
                                                                                                        // content
        // 2. 인증검사
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            throw new CustomApiException("인증이 되지 않았습니다", HttpStatus.UNAUTHORIZED);
        }
        // 3. 유효성검사

        boardService.게시물수정(id); // title / content / userId
        return new ResponseEntity<>(new ResponsDto<>(1, "수정성공", null), HttpStatus.OK);
    }

    @PostMapping("/board/{id}/boardUpdateForm")
    public String update(BoardUpdateReqDto boardUpdateReqDto) {
        User principal = (User) session.getAttribute("principal");
        if (boardUpdateReqDto.getTitle() == null || boardUpdateReqDto.getTitle().isEmpty()) {
            throw new CustomException("Title 작성해주세요");
        }
        if (boardUpdateReqDto.getContent() == null || boardUpdateReqDto.getContent().isEmpty()) {
            throw new CustomException("Content 작성해주세요");
        }
        boardService.게시물수정(boardUpdateReqDto, principal.getId());

    }

    @GetMapping("/board/{id}/boardUpdateForm")
    public String boardUpdateForm(@PathVariable int id) {
        return "board/boardUpdateForm";
    }

}

package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import shop.mtcoding.blog.dto.user.UserReq.JoinReqDto;
import shop.mtcoding.blog.dto.user.UserReq.LoginReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.model.UserRepository;
import shop.mtcoding.blog.service.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user/profileUpdate")
    public String profileUpdate(MultipartFile profile) {
        // 리뷰 MultipartFile profile=> form tag name과 동일

        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }
        if (profile.isEmpty()) { // isEmpty = 공백과 null 값 같이 비교
            throw new CustomException("사진인 전송되지 않았습니다.");
        }
        // 사진이 아니면(터트리기) 위에 코드가 하나 더 필요하다

        // 1. 파일은 하드디스크에 저장
        // import java.nio.file.Path; = 내부 쓰레드가 저장되어 있다 / 쓰레드가없으면 멈춘다
        // Paths.get() 안에 "c:\\workspace\\upload" 파일내부경로로 설정할수 있지만 외부에서 접근하기 힘들다(역슬러쉬가
        // 안되어 두개로 지정(\(x)=> \\(o)))
        // 아래 Path 연결하면 변경된 사진이 images로 들어온다

        // UUID(고유식별자) -> 알고리즘 => TEST해주기
        // 이미지 경로 : /static/images 입니다.

        User userPS = userService.프로필사진수정(profile, principal.getId());
        session.setAttribute("principal", userPS);
        return "redirect:/";
    }

    @GetMapping("/user/profileUpdateForm")
    public String profileUpdateForm(Model model) {
        // 1. 인증
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return "redirect:/loginForm";
        }
        // 2. 기존 사진 select => 조회 (service 이용x)
        User userPS = userRepository.findById(principal.getId());
        // userPS = 영구히 기록되있는 것을 끄집어냄
        model.addAttribute("user", userPS);

        // 3. user / db 에 데이터 확인
        // table
        return "user/profileUpdateForm";
    }

    @PostMapping("/join")
    public String join(JoinReqDto joinReqDto) {
        // System.out.println(joinReqDto.getUsername());
        // System.out.println(joinReqDto.getPassword());
        // System.out.println(joinReqDto.getEmail());

        if (joinReqDto.getUsername() == null || joinReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 작성해주세요");

        }
        if (joinReqDto.getPassword() == null || joinReqDto.getPassword().isEmpty()) {
            throw new CustomException("password 작성해주세요");

        }
        if (joinReqDto.getEmail() == null || joinReqDto.getEmail().isEmpty()) {
            throw new CustomException("email 작성해주세요");

        }
        userService.회원가입(joinReqDto);

        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @PostMapping("/login")
    public String login(LoginReqDto loginReqDto) {
        // controlloer 책임 확인
        if (loginReqDto.getUsername() == null || loginReqDto.getUsername().isEmpty()) {
            throw new CustomException("username을 작성해주세요");
        }
        if (loginReqDto.getPassword() == null || loginReqDto.getPassword().isEmpty()) {
            throw new CustomException("password 작성해주세요");
        }
        User principal = userService.로그인(loginReqDto);
        session.setAttribute("principal", principal);
        return "redirect:/";

    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }

}

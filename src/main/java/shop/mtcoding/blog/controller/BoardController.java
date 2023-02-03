package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.model.BoardRepository;
import shop.mtcoding.blog.model.User;

@Controller
public class BoardController {
    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping({ "/", "/board" })
    public String main() {
        return "board/main";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id) {
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

    @GetMapping("/board/{id}/boardUpdateForm")
    public String boardUpdateForm(@PathVariable int id) {
        return "board/boardUpdateForm";
    }

}

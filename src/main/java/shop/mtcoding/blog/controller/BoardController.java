package shop.mtcoding.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
    @GetMapping("/board/boardUpdateForm")
    public String boardUpdateForm() {
        return "board/boardUpdateForm";
    }

    @GetMapping("/detail")
    public String detail() {
        return "board/detail";
    }

    @GetMapping("/")
    public String main() {
        return "board/main";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

}

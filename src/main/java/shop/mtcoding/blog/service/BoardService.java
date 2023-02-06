package shop.mtcoding.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.board.BoardReq.BoadSaveReqDto;

@Service
public class BoardService {

    @Transactional
    public int 글쓰기(BoadSaveReqDto boadSaveReqDto) {
        return 1;
    }
}

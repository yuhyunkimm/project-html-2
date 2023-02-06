package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.BoardRepository;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    // where절 들어가는 파라미터를 첫번째에 설정 나머지는 뒤에 받는다
    public int 글쓰기(BoardSaveReqDto boadSaveReqDto, int useId) {
        int result = boardRepository.insert(boadSaveReqDto.getTitle(), boadSaveReqDto.getContent(), useId);
        if (result != 1) {
            throw new CustomException("글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return 1;
    }
}

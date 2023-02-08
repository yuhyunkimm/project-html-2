package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.BoardRepository;

@Transactional(readOnly = true)
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Transactional
    // where절 들어가는 파라미터를 첫번째에 설정 나머지는 뒤에 받는다
    public int 글쓰기(BoardSaveReqDto boadSaveReqDto, int useId) {

        // 1. content 내용을 Document로 받고, img 찾아내서 (0,1,2) 0번에 src를 찾아서 thumnail 추가
        // controller에서 코드를 짜고 util 폴더에 옮겨준다
        int result = boardRepository.insert(boadSaveReqDto.getTitle(), boadSaveReqDto.getContent(), null, useId);
        if (result != 1) {
            throw new CustomException("글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return 1;
    }

    @Transactional
    public void 게시글삭제(int id, int userId) {
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("없는 게시글을 삭제할 수 없습니다");
        }
        if (boardPS.getUserId() != userId) {
            throw new CustomApiException("해당 게시글을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        try {
            boardRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            // 로그를 남겨야 함 (DB or File) => 수정을 해줘야 한다
            // e.massage() , 시간 , request => 클래스를 따로 만들어서 관리해야 한다
        }
    }

    @Transactional
    public void 게시글수정(int id, BoardUpdateReqDto boardUpdateReqDto, int principalId) {
        // 부가적인 로직 = AOP(관점지향로직)
        Board boardPS = boardRepository.findById(id);
        if (boardPS == null) {
            throw new CustomApiException("해당 게시글을 찾을 수 없습니다");
        }
        if (boardPS.getUserId() == principalId) {
            throw new CustomApiException("해당 게시글을 수정할 권한이 없습니다", HttpStatus.FORBIDDEN);
        }
        // 핵심로직
        int result = boardRepository.updateById(id, boardUpdateReqDto.getTitle(), boardUpdateReqDto.getContent());
        if (result != 1) {
            throw new CustomApiException("서버에 일시적인 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
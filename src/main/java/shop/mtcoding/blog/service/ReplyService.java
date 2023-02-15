package shop.mtcoding.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog.dto.reply.ReplyReq.ReplySaveReqDto;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.ReplyRepository;

@Slf4j
@Transactional(readOnly = true)
@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Transactional
    public void 댓글쓰기(ReplySaveReqDto replySaveReqDto, int principalId) {

        int result = replyRepository.insert(replySaveReqDto.getComment(), replySaveReqDto.getBoardId(), principalId);

        if (result != 1) {
            throw new CustomApiException("댓글쓰기 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @권한체크
    @Transactional
    public void 댓글삭제(int id, int principalId) {
        // reply = entity
        Reply reply = replyRepository.findById(id);
        // reply 존재 여부
        if (reply.getUserId() != principalId) {
            throw new CustomApiException("댓글이 존재하지 않습니다");
        }
        if (reply.getUserId() != principalId) {
            throw new CustomApiException("댓글을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        // 1. 인증 ok / 2. 댓글 존재 유무 확인 / 3. 권한 ok
        // 부가적인 코드 = AOP
        try {
            replyRepository.deleteById(id);
        } catch (Exception e) {
            log.error("서버에러 : " + e.getMessage()); // bw에 서버에러 메세지를 파일에 저장
            // 버퍼달고 , 파일에 쓰기
            throw new CustomApiException("댓글 삭제 실패 - 서버에러", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
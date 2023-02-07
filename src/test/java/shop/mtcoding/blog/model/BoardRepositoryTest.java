package shop.mtcoding.blog.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;

// f-ds-c : @webmvctest 
// F - DS - C - S - R - MyBatis - DB -> 레이어 책임 분리
// SRP : Sigle Responsbty
@MybatisTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void findAllWithUser_test() throws Exception {
        // given
        ObjectMapper om = new ObjectMapper(); // Jackson => Controller 발동시점

        // when
        // 테스트 1번 -> JSON뷰어로 보기
        List<BoardMainRespDto> BoardMainRespDto = boardRepository.findAllWithUser();
        String responseBody = om.writeValueAsString(BoardMainRespDto);
        System.out.println("테스트 : " + responseBody);

        // 테스트 2번
        // BoardMainRespDto.forEach((dto)->{
        // System.out.println("테스트 : "+dto.getId());
        // System.out.println("테스트 : "+dto.getTitle());
        // System.out.println("테스트 : "+dto.getUsername());
        // });

        // then
        assertThat(BoardMainRespDto.get(5).getUsername()).isEqualTo("love");
    }
}
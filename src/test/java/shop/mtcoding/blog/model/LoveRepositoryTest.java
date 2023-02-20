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
public class LoveRepositoryTest {

    @Autowired
    private LoveRepository loveRepository;

    @Test
    public void findByBoardIdAndUserId_test() throws Exception {
        // given
        // 메서드의 메개변수에 들어오는 데이터를 넣어준다
        int boardId = 1;
        int userId = 1;

        // ObjectMapper는 timstamp 파싱을 못한다
        ObjectMapper om = new ObjectMapper(); // Jackson => Controller 발동시점

        // when
        // PS = Select
        Love lovePS = loveRepository.findByBoardIdAndUserId(boardId, userId);
        // String responseBody = om.writeValueAsString(lovePS);
        // System.out.println("테스트 : " + responseBody);

        // then
        assertThat(lovePS.getBoardId()).isEqualTo(1);
        assertThat(lovePS.getUserId()).isEqualTo(1);
    }
}
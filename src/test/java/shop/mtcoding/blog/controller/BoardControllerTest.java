package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardReq.BoardSaveReqDto;
import shop.mtcoding.blog.dto.board.BoardReq.BoardUpdateReqDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;
import shop.mtcoding.blog.dto.reply.ReplyResp.ReplyDetailRespDto;
import shop.mtcoding.blog.model.User;

/*
 * SpringBootTest는 통합테스트(실제 환경과 동일하게 Bean이 생성됨)
 * AutoConfigureMockMvc는 MOCK 환경의 IoC 컨테이너에 MockMvc Bean이 생성됨
 */

// MOCK = 가짜 환경에 IOC컨테이너가 띄워져 있다 => 실행을 하면 랜덤으로 port설정

// 각각의 테스트는 격리성을 가져야한다
// 메서드 실행 직후 롤백 , 테스트 격리를 위해 필요하다 
// 단점 : auto_increment 초기화
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    // 가짜 인증 만드는 법
    private MockHttpSession mockSession;

    // @BeforeEach // Test 메서드 실행 직전 마다 호출됨
    // @AfterAll
    // @Truncate
    @BeforeEach
    public void setUp() {
        // 데이터 인서트

        // 세션 주입
        User user = new User();
        user.setId(1);
        user.setUsername("ssar");
        user.setPassword("1234");
        user.setEmail("ssar@nate.com");
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        mockSession = new MockHttpSession();
        mockSession.setAttribute("principal", user);
    }

    @Test
    public void update_test() throws Exception {
        // given
        int id = 1;
        BoardUpdateReqDto boardUpdateReqDto = new BoardUpdateReqDto();
        boardUpdateReqDto.setTitle("제목1-수정");
        boardUpdateReqDto.setContent("내용1-수정");

        String requestBody = om.writeValueAsString(boardUpdateReqDto);
        // System.out.println("테스트 : " + requestBody);
        // when
        ResultActions resultActions = mvc.perform(
                put("/board/" + id)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE) // default : utf-8
                        .session(mockSession));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.print("테스트: " + responseBody);
        // then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.code").value(1));
    }

    @Test
    public void delete_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                delete("/board/" + id).session(mockSession));
        String reponseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + reponseBody);

        /*
         * jasonPath
         * 최상위 : $
         * 객체탐색 : (.)닷
         * 배열 : [0]
         * 시작 = $ , 오브젝트{} = . ,
         * ex> $[0].title
         */
        // then
        resultActions.andExpect(jsonPath("$.code").value(1));
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void detail_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                get("/board/" + id));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailRespDto boardDto = (BoardDetailRespDto) map.get("boardDto");
        List<ReplyDetailRespDto> replyDtos = (List<ReplyDetailRespDto>) map.get("replyDtos");
        // String boardJson = om.writeValueAsString(boardDto);
        // String replyListJson = om.writeValueAsString(replyDtos);
        // System.out.println("테스트 : " + boardJson);
        // System.out.println("테스트 : " + replyListJson);

        // then
        resultActions.andExpect(status().isOk());
        assertThat(boardDto.getUsername()).isEqualTo("ssar");
        assertThat(boardDto.getUserId()).isEqualTo(1);
        assertThat(boardDto.getTitle()).isEqualTo("1번째 제목");
        assertThat(replyDtos.get(1).getComment()).isEqualTo("댓글3");
        assertThat(replyDtos.get(1).getUsername()).isEqualTo("love");
    }

    @Test
    public void main_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/"));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        List<BoardMainRespDto> dtods = (List<BoardMainRespDto>) map.get("dtos");
        String model = om.writeValueAsString(dtods);
        System.out.println("테스트 : " + model);

        // then
        // "board/main" = status 200번
        resultActions.andExpect(status().isOk());
        assertThat(dtods.size()).isEqualTo(6);
        assertThat(dtods.get(6).getUsername()).isEqualTo("ssar");
    }

    @Test
    public void save_test() throws Exception {
        // given
        BoardSaveReqDto boardSaveReqDto = new BoardSaveReqDto();
        boardSaveReqDto.setTitle("제목");
        boardSaveReqDto.setContent("내용");

        String requestBody = om.writeValueAsString(boardSaveReqDto);

        // when
        ResultActions resultActions = mvc.perform(
                post("/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession)); // session 주입
        System.out.println("save_test : ");
        // then
        resultActions.andExpect(status().isCreated());
    }
}
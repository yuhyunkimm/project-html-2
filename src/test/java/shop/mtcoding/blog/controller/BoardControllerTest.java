package shop.mtcoding.blog.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.blog.dto.board.BoardResp.BoardDetailRespDto;
import shop.mtcoding.blog.dto.board.BoardResp.BoardMainRespDto;
import shop.mtcoding.blog.model.User;

/*
 * SpringBootTest는 통합테스트(실제 환경과 동일하게 Bean이 생성됨)
 * AutoConfigureMockMvc는 MOCK 환경의 IoC 컨테이너에 MockMvc Bean이 생성됨
 */

// MOCK = 가짜 환경에 IOC컨테이너가 띄워져 있다 => 실행을 하면 랜덤으로 port설정

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    // 가짜 인증 만드는 법
    private MockHttpSession mockSession;

    @BeforeEach // Test 메서드 실행 직전 마다 호출됨
    public void setUp() {
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
    public void delete_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(
                delete("/board/" + id).session(mockSession));
        String reponseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + reponseBody);
        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void detail_test() throws Exception {
        // given
        int id = 1;

        // when
        ResultActions resultActions = mvc.perform(get("/board/" + id));
        Map<String, Object> map = resultActions.andReturn().getModelAndView().getModel();
        BoardDetailRespDto dto = (BoardDetailRespDto) map.get("dto");
        String model = om.writeValueAsString(dto);
        System.out.println("테스트 : " + model);

        // then
        resultActions.andExpect(status().isOk()); // status = 200
        assertThat(dto.getUsername()).isEqualTo("ssar");
        assertThat(dto.getId()).isEqualTo(1);

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
        String title = "";
        for (int i = 0; i < 99; i++) {
            title += "가";
        }
        String requestBody = "title=제목1&content=내용1";

        // when
        ResultActions resultActions = mvc.perform(
                post("/board")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .session(mockSession)); // session 주입

        // then
        resultActions.andExpect(status().is3xxRedirection());
    }
}
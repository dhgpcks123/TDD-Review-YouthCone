package com.example.youthcone21tdd;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
/*
    시나리오
    - 리뷰를 조회할 수 있게 해주세요
    - 리뷰 작성자에게 배민 선물하기 기능 추가해주세요.

    요구사항
    *후기 조회하기 API
    - [ ] 후기의 ID를 요청 값으로 받는다.
    - [ ] 요청 ID에 대한 후기를 찾아 응답값을 내려준다 (200OK)
    - [ ] 응답에는 반드시 본문과 연락처를 포함해야 한다.
    - [ ] 후기가 존재하지 않는다면 에러를 응답해야 한다. (404 Not Found)
    *후기 작성자에게 선물하기 API
    - [ ] 후기의 ID를 요청 값으로 받는다.
    - [ ] 선물은 후기당 한번만 요청할 수 있다.
    - [ ] 선물하기에 성공하면 후기의 현재 상태를 응답한다. (200OK)
    - [ ] 선물하기는 아래의 API 를 호출하여 수행한다.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// embedded 환경 사용, 포트 충돌 일어나지 않음.
class AcceptanceTest {

    @LocalServerPort
    int port;
    //포트 번호 확인 가능

    @Autowired
    ReviewRepository reviewRepository;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
    }

    private Long id = 1L;
    private String content = "재밌어요";
    private String phoneNumber = "010-1111-2222";

    @Test
    void 후기_조회_성공(){
        //given, arrange, 준비
        reviewRepository.save(
                Review.builder()
                        .content(content)
                        .phoneNumber(phoneNumber).build()
        );

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
        //when, act, 실행
        .when()
                .get("/reviews/1")
        //then, assert, 검증
        .then()
                .statusCode(HttpStatus.OK.value())
                .assertThat()
                .body("id", equalTo(id.intValue()))
                .body("content", equalTo(content))
                .body("phoneNumber", equalTo(phoneNumber));
    }
    @Test
    void 후기_조회_실패(){
        //given, arrange, 준비
        reviewRepository.save(
                Review.builder()
                        .content(content)
                        .phoneNumber(phoneNumber).build()
        );

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
        //when, act, 실행
        .when()
                .get("/reviews/1000")
        //then, assert, 검증
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
    @Test
    void 선물하기_성공(){
        reviewRepository.save(
                Review.builder()
                        .content(content)
                        .phoneNumber(phoneNumber)
                        .build()
        );
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
                    .put("/reviews/1")
        .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("isSent", equalTo(true));
    }


    // @author: hyechan, @since: 2022/04/24 2:48 오후
    @Test
    void 선물하기_실패() throws Exception{
        //given
        reviewRepository.save(
                Review.builder()
                        .content(content)
                        .phoneNumber(phoneNumber)
                        .build()
        );
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
        .when()
                .put("reviews/1000")
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

}

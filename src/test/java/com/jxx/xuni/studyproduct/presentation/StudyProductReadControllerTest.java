package com.jxx.xuni.studyproduct.presentation;

import com.jxx.xuni.studyproduct.application.StudyProductReadService;
import com.jxx.xuni.common.domain.Category;
import com.jxx.xuni.studyproduct.domain.Content;
import com.jxx.xuni.studyproduct.dto.response.StudyProductContentReadResponse;
import com.jxx.xuni.studyproduct.dto.response.StudyProductReadResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

import static com.jxx.xuni.ApiDocumentUtils.getDocumentRequest;
import static com.jxx.xuni.ApiDocumentUtils.getDocumentResponse;
import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_DETAIL_READ;
import static com.jxx.xuni.studyproduct.dto.response.StudyProductApiMessage.STUDY_PRODUCT_READ;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(StudyProductControllerTestConfig.class)
public class StudyProductReadControllerTest extends StudyProductCommon{

    @Autowired
    StudyProductReadService studyProductReadService;

    @DisplayName("스터디 상품 전체 조회")
    @Test
    void study_product_read_all() throws Exception {
        StudyProductReadResponse response1 = new StudyProductReadResponse(UUID.randomUUID().toString(),
                "초보 웹 개발자를 위한 스프링5 프로그래밍 입문", Category.SPRING_FRAMEWORK,
                "최범균", "IMAGE URL");
        StudyProductReadResponse response2 = new StudyProductReadResponse(UUID.randomUUID().toString(),
                "미즈구치 카츠야", Category.NETWORK, "모두의 네트워크", "IMAGE URL");
        BDDMockito.given(studyProductReadService.readMany(PageRequest.of(0,10))).willReturn(List.of(response1, response2));

        ResultActions result = mockMvc.perform(get("/study-products")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("size", "10")
                .queryParam("page", "0"));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(STUDY_PRODUCT_READ))

                .andDo(MockMvcRestDocumentation.document("studyproduct/query/readAll",
                        getDocumentRequest(), getDocumentResponse(),

                        queryParameters(
                                parameterWithName("size").description("한 번에 보여줄 상품 개수"),
                                parameterWithName("page").description("현재 페이지")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.ARRAY).description("조회 데이터"),
                                fieldWithPath("response[].studyProductId").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response[].name").type(JsonFieldType.STRING).description("스터디 상품 제목"),
                                fieldWithPath("response[].category").type(JsonFieldType.STRING).description("스터디 상품 카테고리"),
                                fieldWithPath("response[].creator").type(JsonFieldType.STRING).description("스터디 상품 저자"),
                                fieldWithPath("response[].thumbnail").type(JsonFieldType.STRING).description("스터디 상품 썸네일 URL")
                        )
                ));
    }

    @DisplayName("스터디 상품 카테고리별 조회")
    @Test
    void study_product_read_cond_by_category() throws Exception {
        StudyProductReadResponse response1 = new StudyProductReadResponse(UUID.randomUUID().toString(),
                "JAVA의 정석", Category.JAVA, "남궁성", "IMAGE URL");
        StudyProductReadResponse response2 = new StudyProductReadResponse(UUID.randomUUID().toString(),
                "이펙티브 자바 3/E", Category.JAVA, "조슈아 블로치", "IMAGE URL");
        BDDMockito.given(studyProductReadService.readManyBy(Category.JAVA)).willReturn(List.of(response1, response2));

        ResultActions result = mockMvc.perform(get("/study-products/cond")
                .param("category", Category.JAVA.name())
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(Category.JAVA.name() + " " + STUDY_PRODUCT_READ))

                .andDo(MockMvcRestDocumentation.document("studyproduct/query/readCond",
                        getDocumentRequest(), getDocumentResponse(),

                        queryParameters(
                                parameterWithName("category").description("스터디 상품 카테고리")
                        ),

                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.ARRAY).description("조회 데이터"),
                                fieldWithPath("response[].studyProductId").type(JsonFieldType.STRING).description("스터디 상품 식별자"),
                                fieldWithPath("response[].name").type(JsonFieldType.STRING).description("스터디 상품 제목"),
                                fieldWithPath("response[].category").type(JsonFieldType.STRING).description("스터디 상품 카테고리"),
                                fieldWithPath("response[].creator").type(JsonFieldType.STRING).description("스터디 상품 저자"),
                                fieldWithPath("response[].thumbnail").type(JsonFieldType.STRING).description("스터디 상품 썸네일 URL")
                        )
                ));
    }

    @DisplayName("스터디 상품 상세 조회")
    @Test
    void study_product_read_one() throws Exception {
        StudyProductContentReadResponse response = new StudyProductContentReadResponse(
                "모두의 네트워크",
                Category.NETWORK,
                "미즈구치 카츠야",
                "IMAGE URL",
                List.of(new Content(1l, "1장 네트워크 첫걸음"),
                        new Content(2l, "2장 네트워크의 기본 규칙"),
                        new Content(3l, "3장 물리 계층 : 데이터를 전기 신호로 변환하기"),
                        new Content(4l, "4장 데이터 링크 계층 : 랜에서 데이터 전송하기"),
                        new Content(5l, "5장 네트워크 계층 : 목적지에 데이터 전달하기"),
                        new Content(6l, "6장 전송 계층 : 신뢰할 수 있는 데이터 전송하기"),
                        new Content(7l, "7장 응용 계층 : 애플리케이션에 데이터 전송하기"),
                        new Content(8l, "8장 네트워크의 전체 흐름 살펴보기"),
                        new Content(9l, "9장 무선 랜 이해하기")));

        BDDMockito.given(studyProductReadService.readDetailBy(any())).willReturn(response);

        ResultActions result = mockMvc.perform(get("/study-products/{study-product-id}", "uuid")
                .contentType(MediaType.APPLICATION_JSON));

        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(STUDY_PRODUCT_DETAIL_READ))

                .andDo(MockMvcRestDocumentation.document("studyproduct/query/readOne",
                        getDocumentRequest(), getDocumentResponse(),
                        pathParameters(
                                parameterWithName("study-product-id").description("스터디 상품 식별자")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),

                                fieldWithPath("response").type(JsonFieldType.OBJECT).description("조회 데이터"),
                                fieldWithPath("response.name").type(JsonFieldType.STRING).description("스터디 상품 이름"),
                                fieldWithPath("response.category").type(JsonFieldType.STRING).description("스터디 상품 카테고리"),
                                fieldWithPath("response.creator").type(JsonFieldType.STRING).description("스터디 상품 저자"),
                                fieldWithPath("response.thumbnail").type(JsonFieldType.STRING).description("스터디 상품 썸네일 URL"),

                                fieldWithPath("response.contents").type(JsonFieldType.ARRAY).description("스터디 상품 컨텐츠 정보"),
                                fieldWithPath("response.contents[].chapterNo").type(JsonFieldType.NUMBER).description("상품 목차 인덱스"),
                                fieldWithPath("response.contents[].title").type(JsonFieldType.STRING).description("목차 제목")
                        )
                ));
    }
}

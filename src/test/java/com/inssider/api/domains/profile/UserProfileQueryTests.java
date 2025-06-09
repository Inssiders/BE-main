package com.inssider.api.domains.profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.common.Util;
import com.inssider.api.domains.account.Account;
import com.inssider.api.domains.account.TestAccountRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileQueryTests {

  @Autowired private TestAccountRepository accountRepository;
  @Autowired private UserProfileRepository repository;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @Transactional
  void 사용자_프로필_기본_조회() throws Exception {
    // Given
    List<Account> accounts =
        Stream.generate(Util.accountGenerator()::get).limit(8).collect(Collectors.toList());
    accountRepository.saveAllAndFlush(accounts);

    // 프로필 설정 변경
    List<UserProfile> profiles = repository.findAll();
    IntStream.range(0, profiles.size()).forEach(i -> profiles.get(i).setAccountVisible(i < 4));
    repository.saveAllAndFlush(profiles);

    // When
    var response =
        mockMvc
            .perform(get("/api/profiles"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Then
    var jsonNode = objectMapper.readTree(response);
    JsonNode items = jsonNode.get("items");

    // 개수 검증
    assertEquals(4, streamOf(items).filter(node -> node.has("bio")).count());
    assertEquals(4, streamOf(items).filter(node -> !node.has("bio")).count());

    // 각 프로필의 필드 검증
    assertTrue(
        streamOf(items)
            .filter(node -> node.has("bio"))
            .allMatch(node -> node.has("nickname") && node.has("profile_url")));

    assertTrue(streamOf(items).noneMatch(node -> node.has("accountVisible")));

    // 페이지네이션 정보 검증
    JsonNode pagination = jsonNode.get("pagination");
    assertEquals(8, pagination.get("total_items").asInt());
    assertEquals(1, pagination.get("total_pages").asInt());
    assertEquals(0, pagination.get("current_page").asInt());
    assertEquals(false, pagination.get("has_next").asBoolean());
    assertEquals(false, pagination.get("has_prev").asBoolean());
  }

  private Stream<JsonNode> streamOf(JsonNode arrayNode) {
    return StreamSupport.stream(arrayNode.spliterator(), false);
  }
}

package com.inssider.api.domains.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountRequestsDto.RegisterRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RegisterTypeJsonTest {

  @Autowired private ObjectMapper objectMapper;

  @Test
  void registerType_JSON_역직렬화_테스트() throws Exception {
    String json =
        """
        {
          "register_type": "PASSWORD",
          "email": "test@gmail.com",
          "password": "1@qQ12323"
        }
        """;

    RegisterRequestDto result = objectMapper.readValue(json, RegisterRequestDto.class);

    assertEquals(RegisterType.PASSWORD, result.registerType());
    assertEquals("test@gmail.com", result.email());
    assertEquals("1@qQ12323", result.password());
  }

  @Test
  void registerType_대소문자_테스트() throws Exception {
    String json =
        """
        {
          "registerType": "password",
          "email": "test@gmail.com",
          "password": "1@qQ12323"
        }
        """;

    RegisterRequestDto result = objectMapper.readValue(json, RegisterRequestDto.class);

    assertNull(result.registerType()); // 대소문자 구분 없이 들어오면 null로 처리됨
  }

  @Test
  void registerType_직접_변환_테스트() {
    RegisterType result = RegisterType.valueOf("PASSWORD");
    assertEquals(RegisterType.PASSWORD, result);

    RegisterType result2 = RegisterType.valueOf("password".toUpperCase());
    assertEquals(RegisterType.PASSWORD, result2);
  }
}

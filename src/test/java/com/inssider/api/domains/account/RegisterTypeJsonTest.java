package com.inssider.api.domains.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inssider.api.domains.account.AccountDataTypes.RegisterType;
import com.inssider.api.domains.account.AccountRequestsDto.PostAccountRequest;
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

    PostAccountRequest result = objectMapper.readValue(json, PostAccountRequest.class);

    assertEquals(RegisterType.PASSWORD, result.registerType());
    assertEquals("test@gmail.com", result.email());
    assertEquals("1@qQ12323", result.password());
  }

  @Test
  void registerType_camelCase_필드명_테스트() throws Exception {
    String json =
        """
        {
          "registerType": "PASSWORD",
          "email": "test@gmail.com",
          "password": "1@qQ12323"
        }
        """;

    PostAccountRequest result = objectMapper.readValue(json, PostAccountRequest.class);

    assertNull(result.registerType()); // camelCase 필드명은 인식되지 않아 null로 처리됨
  }

  @Test
  void registerType_소문자_값_테스트() throws Exception {
    String json =
        """
        {
          "register_type": "password",
          "email": "test@gmail.com",
          "password": "1@qQ12323"
        }
        """;

    assertThrows(
        com.fasterxml.jackson.databind.exc.InvalidFormatException.class,
        () ->
            objectMapper.readValue(
                json, PostAccountRequest.class) // "password"는 RegisterType enum에 정의된 값이 아니므로 예외 발생
        );
  }
}

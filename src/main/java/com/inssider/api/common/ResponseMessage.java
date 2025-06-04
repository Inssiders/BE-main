package com.inssider.api.common;

import org.springframework.http.HttpStatus;

/** HTTP 응답 상태 코드에 따른 기본 메시지 정의 */
public enum ResponseMessage {

  // 2xx: 성공
  OK(HttpStatus.OK, "요청이 성공적으로 처리되었습니다"),
  CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다"),
  ACCEPTED(HttpStatus.ACCEPTED, "요청이 접수되었지만 아직 처리되지 않았습니다"),
  NO_CONTENT(HttpStatus.NO_CONTENT, "요청이 성공했으며 반환할 컨텐츠가 없습니다"),

  // 3xx: 리다이렉션
  MOVED_PERMANENTLY(HttpStatus.MOVED_PERMANENTLY, "요청한 리소스가 영구적으로 이동했습니다"),
  FOUND(HttpStatus.FOUND, "요청한 리소스가 일시적으로 이동했습니다"),
  SEE_OTHER(HttpStatus.SEE_OTHER, "요청한 리소스를 다른 URI에서 찾아야 합니다"),
  NOT_MODIFIED(HttpStatus.NOT_MODIFIED, "리소스가 변경되지 않았습니다"),
  TEMPORARY_REDIRECT(HttpStatus.TEMPORARY_REDIRECT, "요청을 다른 URI로 임시 리다이렉션합니다"),

  // 4xx: 클라이언트 오류
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
  FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
  NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다"),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다"),
  CONFLICT(HttpStatus.CONFLICT, "요청이 현재 리소스 상태와 충돌합니다"),
  UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다"),
  TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "요청이 너무 많습니다"),

  // 5xx: 서버 오류
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다"),
  NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "요청한 기능이 구현되지 않았습니다"),
  BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "게이트웨이 오류가 발생했습니다"),
  SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "서비스를 일시적으로 사용할 수 없습니다"),
  GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "게이트웨이 시간 초과가 발생했습니다");

  private final HttpStatus status;
  private final String message;

  ResponseMessage(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public int getStatusCode() {
    return status.value();
  }

  public String getMessage() {
    return message;
  }

  /** HttpStatus로 ResponseMessage 찾기 */
  public static ResponseMessage of(HttpStatus status) {
    return of(status.value());
  }

  /** 상태 코드로 ResponseMessage 찾기 */
  public static ResponseMessage of(int statusCode) {
    for (ResponseMessage message : values()) {
      if (message.status.value() == statusCode) {
        return message;
      }
    }
    return INTERNAL_SERVER_ERROR; // 기본값
  }
}

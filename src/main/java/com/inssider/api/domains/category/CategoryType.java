package com.inssider.api.domains.category;

public enum CategoryType {
  KPOP(1, "K-POP"),
  ENTERTAINMENT(2, "예능"),
  DRAMA(3, "드라마"),
  INFLUENCER(4, "인플루언서"),
  NEWS(5, "시사 뉴스"),
  MOVIE(6, "영화"),
  ANIMATION(7, "애니메이션"),
  CHALLENGE(8, "챌린지"),
  NEW_SLANG(9, "신조어"),
  TRENDING(10, "유행어"),
  ETC(99, "기타"),
  USER_CONTENTS(999, "공감 밈");

  private final int code;
  private final String displayName;

  CategoryType(int code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  public int getCode() {
    return code;
  }

  public String getDisplayName() {
    return displayName;
  }
}

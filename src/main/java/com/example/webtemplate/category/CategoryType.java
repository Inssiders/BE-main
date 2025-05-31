package com.example.webtemplate.category;

public enum CategoryType {
    DANCE("힙합"),
    BOOKS("도서"),
    FOOD("먹방"),
    SPORTS("스포츠"),
    HOME("집순이");

    private final String displayName;

    CategoryType(String displayName) {
        this.displayName = displayName;
    }

}
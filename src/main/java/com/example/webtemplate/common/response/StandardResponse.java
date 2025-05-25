package com.example.webtemplate.common.response;

import java.util.List;

public class StandardResponse {

    public record IndexResponse(List<Long> index) {

    }
}

package com.example.webtemplate.common.response;

import java.util.List;

public class BaseResponse {

    public record IndexResponse(List<Long> index) {

    }
}

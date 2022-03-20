package com.musinsa.musinsaapi.response.dto.cateogy;

import com.musinsa.musinsaapi.response.RequestResult;
import lombok.Getter;

@Getter
public class Response {
    private RequestResult result;
    private String message;

    public Response(RequestResult result, String message) {
        this.result = result;
        this.message = message;
    }
}

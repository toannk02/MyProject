package com.example.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class HttpResponse<T> implements Serializable {
    private T data;
    private Integer statusCode;
    private String message;

//    public static <T> HttpResponse<T> ok() {
//        return new HttpResponse<>(null, HttpStatus.OK.value(), "success");
//    }

    public static <T> HttpResponse<T> ok(T data) {
        return new HttpResponse<>(data, HttpStatus.OK.value(), "success");
    }
}

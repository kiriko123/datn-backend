package com.example.demospringsecurity.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestResponse<T> {
    private int statusCode;
    private String error;

    // message có thể là string, hoặc arrayList
    private Object message;
    private T data;
}

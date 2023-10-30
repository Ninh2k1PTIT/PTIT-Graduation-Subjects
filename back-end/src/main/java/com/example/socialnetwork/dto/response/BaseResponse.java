package com.example.socialnetwork.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private T data;
    private Boolean success;
    private String userMessage;
    private String debugMessage;
}

package io.github.miguel.quarkus.rest.dto;

import lombok.Data;

@Data
public class CreatePostRequest {
    private String text;
}

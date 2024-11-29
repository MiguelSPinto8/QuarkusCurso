package io.github.miguel.quarkus.rest.dto;

import io.github.miguel.quarkus.domain.model.Follower;
import lombok.Data;

@Data
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse(){

    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public FollowerResponse(Follower follower){
        this(follower.getId(), follower.getFollower().getName());
    }
}

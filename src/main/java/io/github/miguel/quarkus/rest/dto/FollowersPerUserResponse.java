package io.github.miguel.quarkus.rest.dto;

import io.github.miguel.quarkus.domain.model.Follower;
import lombok.Data;

import java.util.List;
@Data
public class FollowersPerUserResponse {
    private Integer followersCount;
    private List<FollowerResponse> content;
}

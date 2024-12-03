package io.github.miguel.quarkus.rest;

import io.github.miguel.quarkus.domain.model.Follower;
import io.github.miguel.quarkus.domain.model.Post;
import io.github.miguel.quarkus.domain.model.User;
import io.github.miguel.quarkus.domain.repository.FollowerRepository;
import io.github.miguel.quarkus.domain.repository.PostRepository;
import io.github.miguel.quarkus.domain.repository.UserRepository;
import io.github.miguel.quarkus.rest.dto.CreatePostRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;
    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach
    @Transactional
    public void setUp(){
        var user = new User();
        user.setAge(30);
        user.setName("Miguel");
        userRepository.persist(user);
        userId=user.getId();

        Post post =new Post();
        post.setText("Hello");
        post.setUser(user);
        postRepository.persist(post);


        var userNotFollower = new User();
        userNotFollower.setAge(32);
        userNotFollower.setName("Miguel2");
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        var userFollower = new User();
        userFollower.setAge(34);
        userFollower.setName("Miguel3");
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);



    }

    @Test
    @DisplayName("should create a post for a user")
    public void createPostTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some txt");

        var userID = 1;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userID)
                .when()
                .post()
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("should return 404 when trying to make a post for a nonexistent user")
    public void postForAnNonexistentUserTest(){
        var postRequest = new CreatePostRequest();
        postRequest.setText("Some txt");

        var nonexistentUserID = 111;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", nonexistentUserID)
                .when()
                .post()
                .then()
                .statusCode(404);
    }
    @Test
    @DisplayName("should return 404 when user doesn't exist")
    public void listPostsUserNotFoundTest(){
        var nonexistentUserID = 111;

        given()
                .pathParam("userId", nonexistentUserID)
                .when()
                .get()
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("should return 400 when followerId header is not present")
    public void listPostsFollowerHeaderNotSentTest(){
        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("You forgot the header followerId"));



    }
    @Test
    @DisplayName("should return 400 when follower doesn't exist")
    public void listPostsFollowerNotFoundTest(){
        var nonexistentUserID = 999;


        given()
                .pathParam("userId", userId)
                .header("followerId", nonexistentUserID)
                .when()
                .get()
                .then()
                .statusCode(400)
                .body(Matchers.is("Nonexistent followerId"));


    }

    @Test
    @DisplayName("should return 403 when follower doesn't follow")
    public void listPostsNotAFollowerTest(){
        given()
                .pathParam("userId", userId)
                .header("followerId", userNotFollowerId)
                .when()
                .get()
                .then()
                .statusCode(403)
                .body(Matchers.is("You can't see these posts"));

    }
    @Test
    @DisplayName("should return posts")
    public void listPostsTest(){
        given()
                .pathParam("userId", userId)
                .header("followerId", userFollowerId)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(1));

    }


}
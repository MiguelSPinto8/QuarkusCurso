package io.github.miguel.quarkus.domain.repository;

import io.github.miguel.quarkus.domain.model.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository <Post>{
}

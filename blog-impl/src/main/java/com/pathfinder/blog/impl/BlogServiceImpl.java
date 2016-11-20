package com.pathfinder.blog.impl;

import akka.Done;
import akka.NotUsed;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;
import com.pathfinder.blog.api.BlogService;
import com.pathfinder.blog.api.PostContent;

import java.util.Optional;
import java.util.UUID;

/**
 * Service implementation for the blog microservice. This service is essentially a wrapper for the
 * persistence entity API.
 */
public class BlogServiceImpl implements BlogService {

    //injected by Guice
    private final PersistentEntityRegistry registry;

    @Inject
    public BlogServiceImpl(final PersistentEntityRegistry registry) {
        this.registry = registry;
        registry.register(BlogEntity.class);
    }

    @Override
    public ServiceCall<NotUsed, Optional<PostContent>> getPost(String id) {
        return request -> registry.refFor(BlogEntity.class, id)
                .ask(BlogCommand.GetPost.INSTANCE);
    }

    @Override
    public ServiceCall<PostContent, String> addPost() {
        return content -> registry.refFor(BlogEntity.class, UUID.randomUUID().toString())
                .ask(new BlogCommand.AddPost(content));
    }

    @Override
    public ServiceCall<PostContent, Done> updatePost(String id) {
        return content -> registry.refFor(BlogEntity.class, id)
                .ask(new BlogCommand.UpdatePost(content));
    }
}

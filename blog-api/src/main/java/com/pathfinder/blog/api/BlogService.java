package com.pathfinder.blog.api;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import java.util.Optional;

//this is important for the descriptor and it wont autoimport!
import static com.lightbend.lagom.javadsl.api.Service.*;

/**
 * Exposes the blog microservice API.
 */
public interface BlogService extends Service {

    ServiceCall<NotUsed, Optional<PostContent>> getPost(String id);

    ServiceCall<PostContent, String> addPost();

    ServiceCall<PostContent, Done> updatePost(String id);

    @Override
    default Descriptor descriptor() {
        return named("blog").withCalls(
                restCall(Method.GET, "/api/blog/:id", this::getPost),
                restCall(Method.POST, "/api/blog/", this::addPost),
                restCall(Method.PUT, "/api/blog/:id", this::updatePost)
        ).withAutoAcl(true);
    }


}

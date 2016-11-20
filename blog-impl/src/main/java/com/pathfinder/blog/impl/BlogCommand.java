package com.pathfinder.blog.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import com.pathfinder.blog.api.PostContent;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;
import akka.Done;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

/**
 * Commands for manipulating Blog Post entities.
 */
public interface BlogCommand extends Jsonable {

    enum GetPost implements BlogCommand, PersistentEntity.ReplyType<Optional<PostContent>> {
        INSTANCE
    }

    @Immutable
    @JsonDeserialize
    @Value
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    final class AddPost implements BlogCommand, CompressedJsonable, PersistentEntity.ReplyType<String> {
        @NonNull
        PostContent content;
    }

    @Immutable
    @JsonDeserialize
    @Value
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    final class UpdatePost implements BlogCommand, CompressedJsonable, PersistentEntity.ReplyType<Done> {
        @NonNull
        PostContent content;
    }
}

package com.pathfinder.blog.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.pathfinder.blog.api.PostContent;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.annotation.concurrent.Immutable;
import java.util.Optional;

/**
 * Created by Neil on 11/19/2016.
 */
@Immutable
@JsonDeserialize
@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class BlogState implements CompressedJsonable {

    public static final BlogState EMPTY = new BlogState(Optional.empty());

    Optional<PostContent> content;

}

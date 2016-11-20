package com.pathfinder.blog.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.CompressedJsonable;
import com.lightbend.lagom.serialization.Jsonable;
import com.pathfinder.blog.api.PostContent;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.concurrent.Immutable;

/**
 * Events related to blog post entities.  Extending AggregateEvent allows us to shard it across databases.
 */
public interface BlogEvent extends Jsonable, AggregateEvent<BlogEvent> {

    @Override
    default AggregateEventTagger<BlogEvent> aggregateTag() {
        //use the name of the class as the aggregation key.. not great but ok for this demo..
        return AggregateEventTag.of(BlogEvent.class);
    }

    @Immutable
    @JsonDeserialize
    @Value
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    final class PostAdded implements BlogEvent, CompressedJsonable {
        @NonNull
        String id;
        @NonNull
        PostContent content;
    }

    @Immutable
    @JsonDeserialize
    @Value
    @AllArgsConstructor(onConstructor = @__(@JsonCreator))
    final class PostUpdated implements BlogEvent, CompressedJsonable {
        @NonNull
        String id;
        @NonNull
        PostContent content;
    }

}

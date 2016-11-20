package com.pathfinder.blog.impl;

import akka.Done;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.util.Optional;

/**
 * Entity and behaviors for blog posts.  This contains data and behaviour.?
 */
public class BlogEntity extends PersistentEntity<BlogCommand, BlogEvent, BlogState> {


    @Override
    public Behavior initialBehavior(Optional<BlogState> snapshotState) {

        final BehaviorBuilder builder = newBehaviorBuilder(snapshotState.orElse(BlogState.EMPTY));
        addBehaviorForGetPost(builder);
        addBehaviorForAddPost(builder);
        addBehaviorForUpdatePost(builder);
        return builder.build();
    }

    //a simple read only handler that replies with the current state of the entity
    private void addBehaviorForGetPost(BehaviorBuilder builder) {

        //state is a magic variable that is inherited from the PersistentEntity
        //not sure where cmd/ctx come from?
        builder.setReadOnlyCommandHandler(BlogCommand.GetPost.class,
                (cmd,ctx) -> ctx.reply(state().getContent()));

    }

    private void addBehaviorForUpdatePost(BehaviorBuilder builder) {

        //handle the 'UpdatePost' command by persisting a new PostUpdated BlogEvent to the event log,
        // (the id of which comes from?? and the content of which comes from the incoming command.
        //A 'done' is returned to the caller - akka equivilent of HTTP 200
        builder.setCommandHandler(BlogCommand.UpdatePost.class,
                (cmd,ctx) -> ctx.thenPersist(
                        new BlogEvent.PostUpdated(entityId(), cmd.getContent()),
                        evt -> ctx.reply(Done.getInstance())
                )
        );

        //Add an event handler to update the current state of the object when a post updated event is received.
        builder.setEventHandler(BlogEvent.PostUpdated.class,
                evt -> new BlogState(Optional.of(evt.getContent()))
        );
    }

    private void addBehaviorForAddPost(BehaviorBuilder builder) {

        //handle the 'AddPost' command by persisting a new PostAdded BlogEvent to the event log,
        // (the id of which comes from?? and the content of which comes from the incoming command.
        //The entity id is returned to the caller.
        builder.setCommandHandler(BlogCommand.AddPost.class,
                (cmd,ctx) -> ctx.thenPersist(
                        new BlogEvent.PostAdded(entityId(), cmd.getContent()),
                        evt -> ctx.reply(entityId())
                )
        );

        //Add an event handler to update the current state of the object when a post added event is received.
        builder.setEventHandler(BlogEvent.PostAdded.class,
                evt -> new BlogState(Optional.of(evt.getContent()))
        );
    }
}

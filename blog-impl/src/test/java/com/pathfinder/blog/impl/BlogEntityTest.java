package com.pathfinder.blog.impl;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver;
import com.pathfinder.blog.api.PostContent;
import org.junit.*;
import org.junit.rules.TestName;
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver.Outcome;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test to confirm the BlogEntity behaves as expected.
 */
public class BlogEntityTest {

    //entities are actor based.. but how exactly?
    private static ActorSystem system;

    //setup an in memory persistent entity system (wires up commands, events and state for our target system).
    private PersistentEntityTestDriver<BlogCommand, BlogEvent, BlogState> driver;

    //An easy way of dynamically getting the name of the test method being run.
    @Rule
    public TestName testName = new TestName();

    @BeforeClass
    public static void beforeClass() {
        system = ActorSystem.create("BlogEntityTest");
    }

    @AfterClass
    public static void afterClass() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Before
    public void setUp() throws Exception {
        // given a default BlogEntity
        driver = new PersistentEntityTestDriver<>(
                system, new BlogEntity(), testName.getMethodName());
    }

    @Test
    public void initialStateShouldBeEmpty() {

        //when we send a GetPost command
        final Outcome<BlogEvent, BlogState> getPostOutcome = driver.run(BlogCommand.GetPost.INSTANCE);

        //then no events should have been created
        assertThat(getPostOutcome.events()).isEmpty();


        //and the state should still be empty
        assertThat(getPostOutcome.state().getContent()).isEmpty();

        // and we should get back an empty Optional to indicate that
        // no post was found
        final Optional<PostContent> actual = getFirstReply(getPostOutcome);
        assertThat(actual).isNotPresent();
    }

    @Test
    public void shouldBeAbleToAddAPost() {

        // given entity ID of test name
        final String expectedEntityId = testName.getMethodName();

        //when we send an AddPost command
        final Outcome<BlogEvent, BlogState> addPostOutcome = driver.run(new BlogCommand.AddPost(newPostContent()));     ;

        //then a Post Added events should have been created.
        assertThat(addPostOutcome.events()).containsExactly(new BlogEvent.PostAdded(expectedEntityId, newPostContent()));

        // and that the post state of the blog content is the newly posted content.
        assertThat(addPostOutcome.state().getContent()).hasValue(newPostContent());

        //and that the reply gives is the new entity id (and a populated optional containing the post content?).
        final String newEntityId = getFirstReply(addPostOutcome);
        assertThat(newEntityId).isEqualTo(expectedEntityId);

        //and when we send a GetPost command then the reply should be the post we just created.
        //(how do we specify that we want a specific blog post?  - how do we pass the id?
        final Outcome<BlogEvent, BlogState> getPostOutcome = driver.run(BlogCommand.GetPost.INSTANCE);

        final Optional<PostContent> retrievedContent = getFirstReply(getPostOutcome);
        assertThat(retrievedContent).hasValue(newPostContent());

    }

    private PostContent newPostContent() {
        return PostContent.builder().title("A sample post")
                .body("Some sample post body")
                .author("The sample author")
                .build();
    }

    @SuppressWarnings("unchecked")
    private static <T> T getFirstReply(final Outcome<?, ?> outcome) {
        return (T) outcome.getReplies().get(0);
    }



}

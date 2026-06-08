package technischools.projekt4.event;

import org.springframework.context.ApplicationEvent;
import technischools.projekt4.model.Post;

public class PostPublishedEvent extends ApplicationEvent {
    private final Post post;

    public PostPublishedEvent(Object source, Post post) {
        super(source);
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}

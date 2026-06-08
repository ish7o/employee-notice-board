package technischools.projekt4.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import technischools.projekt4.event.PostPublishedEvent;
import technischools.projekt4.exception.ResourceNotFoundException;
import technischools.projekt4.model.Post;
import technischools.projekt4.model.PostCategory;
import technischools.projekt4.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class PostService implements PostServiceInterface {
    private final PostRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public PostService(PostRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public Post getPostById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post with id " + id + " not found"));
    }

    public List<Post> getAllPosts() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }

    public List<Post> getPostsByTitleOrCategory(String title, PostCategory category) {
        return repository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(title, category.name());
    }

    public List<Post> getPinnedPosts() {
        return repository.findByPinnedIsTrue();
    }

    public Post createPost(Post post) {
        post.setCreatedAt(LocalDateTime.now());
        Post savedPost = repository.save(post);
        eventPublisher.publishEvent(new PostPublishedEvent(this, savedPost));
        return savedPost;
    }

    public Post updatePost(Long id, Post post) {
        Post oldPost = this.getPostById(id);
        oldPost.setTitle(post.getTitle());
        oldPost.setAuthor(post.getAuthor());
        oldPost.setPinned(post.getPinned());
        oldPost.setCategory(post.getCategory());
        oldPost.setContent(post.getContent());
        return repository.save(oldPost);
    }

    public Post deletePost(Long id) {
        Post post = this.getPostById(id);
        repository.deleteById(id);
        return post;
    }
}


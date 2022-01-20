package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;

// Stub
@Repository
public class PostRepository {
  
  private final Map<Long, Post> posts;
  private Long index;

  public PostRepository() {
    posts = new HashMap<>();
    index = -1L;
  }

  public List<Post> all() {
//    Comparator<Post> comparator = (o1, o2) -> (int) (o1.getId() - o2.getId());
    List<Post> result = new ArrayList<>();
    result.addAll(posts.values());
    return result;
  }

  public Optional<Post> getById(long id) {
    if (posts.containsKey(id))
      return Optional.ofNullable(posts.get(id));
    else
      return null;
  }

  public synchronized Post save(Post post) {
    index++;
    posts.put(index, post);
    post.setId(index);
    return post;
  }

  public synchronized void removeById(long id) {
    if (posts.containsKey(id))
      posts.remove(id);
  }
}

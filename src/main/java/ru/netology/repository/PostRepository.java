package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
  
  private final Map<Long, Post> posts;
  private AtomicLong index;

  public PostRepository() {
    posts = new ConcurrentSkipListMap<>();
    index = new AtomicLong(-1L);
  }

  public List<Post> all() {
//    Comparator<Post> comparator = (o1, o2) -> (int) (o1.getId() - o2.getId());
    List<Post> result = new ArrayList<>();
    result.addAll(posts.values());
    return result;
  }

  public Optional<Post> getById(long id) throws NotFoundException {
    if (posts.containsKey(id))
      return Optional.ofNullable(posts.get(id));
    else
      throw new NotFoundException();
  }

  public Post save(long id, Post post) {
    if (posts.containsKey(id)){
      posts.put(id, post);
      post.setId(id);
      return post;
    }else{
      throw new NotFoundException();
    }
  }
  public Post save(Post post) {
    long localIndex = index.incrementAndGet();
    posts.put(localIndex, post);
    post.setId(localIndex);
    return post;
  }

  public void removeById(long id) {
    if (posts.containsKey(id))
      posts.remove(id);
  }
}

package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.exception.BadRequestException;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    final List<Post> data = service.all();
    final Gson gson = new Gson();
    response.getWriter().print(gson.toJson(data));
  }

  public void getById(long id, HttpServletResponse response)  throws IOException, NotFoundException {
    response.setContentType(APPLICATION_JSON);

    final Post data =  service.getById(id);
    final Gson gson = new Gson();
    response.getWriter().print(gson.toJson(data));
  }

  public void save(Reader body, HttpServletResponse response) throws IOException, BadRequestException {
    response.setContentType(APPLICATION_JSON);
    final Gson gson = new Gson();
    final Post post = gson.fromJson(body, Post.class);
    if (post.getId() != 0L)
      throw  new BadRequestException();
    final Post data = service.save(post);
    response.getWriter().print(gson.toJson(data));
  }

  public void save(long id, Reader body, HttpServletResponse response) throws IOException, NotFoundException, BadRequestException{
    response.setContentType(APPLICATION_JSON);
    final Gson gson = new Gson();
    final Post post = gson.fromJson(body, Post.class);
    if (post.getId() != id && post.getId() != 0L )
      throw  new BadRequestException();
    if (id == 0L )
      throw  new BadRequestException();
    final Post data = service.save(id, post);
    response.getWriter().print(gson.toJson(data));
  }

  public void removeById(long id, HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON);
    service.removeById(id);
    response.getWriter().print("");
  }
}

package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final String DELETE = "DELETE";
  private PostController controller;

  @Override
  public void init() {
    final PostRepository repository = new PostRepository();
    final PostService service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого

    final String path = req.getRequestURI();
    final String method = req.getMethod();
    long id;
    if(path.matches("/servlets_war_exploded/api/posts/\\d+"))
      id = Long.parseLong(path.substring(path.lastIndexOf("/")));
    else
      id = 0L;

    try {
      switch (method){
        case GET: {
          get (id, resp);
          break;
        }
        case POST: {
          post(req, resp);
          break;
        }
        case DELETE: {
          delete(id, resp);
          break;
        }
        default:{
          resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private void delete(long id, HttpServletResponse response) throws IOException {
    if (id != 0L)
      controller.removeById(id, response);
  }

  private void post(HttpServletRequest req, HttpServletResponse response) throws IOException  {
    controller.save(req.getReader(), response);
  }

  private void get(long id, HttpServletResponse response) throws IOException {
    if (id != 0L)
      controller.getById(id, response);
    else {
     controller.all(response);
    }
  }
}


package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

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
    try {
      final String path = req.getRequestURI();
      final String method = req.getMethod();
      long id;
      if(path.matches("/api/posts/\\d+"))
        id = Long.parseLong(path.substring(path.lastIndexOf("/")));
      else
        id = 0L;

      Map<String, Consumer<HttpServletResponse>> handlers = new HashMap<>();
      handlers.put(GET, (response) -> {
        if (id != 0L)
          controller.getById(id, response);
        else {
          try {
            controller.all(response);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });
      handlers.put(POST, (response) -> {
        try {
          controller.save(req.getReader(), response);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      handlers.put(DELETE, (response) -> {
        if (id != 0L)
          controller.removeById(id, response);
      });

      Consumer<HttpServletResponse> myConsumer = handlers.get(method);
      if (myConsumer != null)
        myConsumer.accept(resp);

//      // primitive routing
//      if (method.equals(GET) && path.equals("/api/posts")) {
//        controller.all(resp);
//        return;
//      }
//      if (method.equals(GET) && path.matches("/api/posts/\\d+")) {
//        // easy way
//        controller.getById(id, resp);
//        return;
//      }
//      if (method.equals(POST) && path.equals("/api/posts")) {
//        controller.save(req.getReader(), resp);
//        return;
//      }
//      if (method.equals(DELETE) && path.matches("/api/posts/\\d+")) {
//        // easy way
//        controller.removeById(id, resp);
//        return;
//      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}


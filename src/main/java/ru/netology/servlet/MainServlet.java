package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    PostController postController;
    public static final String API_POSTS = "/api/posts";
    public static final String API_POSTS_D = "/api/posts/\\d+";
    public static final String STR = "/";
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    public static final String DELETE_METHOD = "DELETE";

    @Override
    public void init() throws ServletException {
        final var context = new AnnotationConfigApplicationContext("ru.netology");

        final var controller = context.getBean("postContriller");

        final var service= context.getBean(PostService.class);

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse response) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            final var id = getIdFromUrl(path);

            if (method.equals(GET_METHOD) && path.equals(API_POSTS)) {
                postController.all(response);
                return;
            }
            if (method.equals(GET_METHOD) && path.matches(API_POSTS_D)) {
                postController.getById(id, response);
                return;
            }
            if (method.equals(POST_METHOD) && path.equals(API_POSTS)) {
                postController.save(req.getReader(),response);
            }
            if (method.equals(DELETE_METHOD) && path.matches(API_POSTS_D)) {
                postController.removeById(id, response);
            }
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException ioException) {
            ioException.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private long getIdFromUrl(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}

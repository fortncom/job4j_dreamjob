package ru.job4j.dream.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IndexServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(IndexServlet.class.getName());
    private final Store store = PsqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", store.findCandidatesByDate(1));
        req.setAttribute("posts", store.findPostsByDate(1));
        LOG.info("ServletIndex.candidate: " + store.findCandidatesByDate(1));
        LOG.info("ServletIndex.posts: " + store.findPostsByDate(1));
        req.getRequestDispatcher("/welcome/index.jsp").forward(req, resp);
    }
}

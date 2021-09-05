package ru.job4j.dream.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CandidateServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CandidateServlet.class.getName());
    private final Store store = PsqlStore.instOf();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("candidates", store.findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Candidate candidate = new Candidate(Integer.parseInt(req.getParameter("id")),
                req.getParameter("name"), store.findCityByName(req.getParameter("city")),
                Timestamp.valueOf(LocalDateTime.now()));
//        LOG.info("city: " + candidate.getCity().getName()
//                + " id: " + candidate.getId() + " name: " + candidate.getName());
        store.save(candidate);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
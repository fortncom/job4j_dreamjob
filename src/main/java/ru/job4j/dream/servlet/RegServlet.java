package ru.job4j.dream.servlet;

import ru.job4j.dream.model.User;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Store store = PsqlStore.instOf();
        User user = store.findUserByEmail(email);
        if (user == null) {
            store.save(new User(0, name, email, password));
            User newUser = store.findUserByEmail(email);
            HttpSession sc = req.getSession();
            sc.setAttribute("user", newUser);
            resp.sendRedirect(req.getContextPath() + "/posts.do");
        } else {
            req.setAttribute("error", "Данный email уже был зарегистрирован.");
            req.getRequestDispatcher("reg.jsp").forward(req, resp);
        }
    }
}

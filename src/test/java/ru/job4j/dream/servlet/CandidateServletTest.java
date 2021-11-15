package ru.job4j.dream.servlet;

import org.hamcrest.core.Is;
import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.store.MemStore;
import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CandidateServletTest {

    @Test
    public void whenCreateCandidate() throws IOException, ServletException {
        Store store = PsqlStore.instOf();
        City city = new City(0, "York");
        store.save(city);

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        when(req.getParameter("id")).thenReturn("0");
        when(req.getParameter("name")).thenReturn("new Candidate");
        when(req.getParameter("city")).thenReturn("York");

        new CandidateServlet().doPost(req, resp);

        Candidate result = store.findCanById(1);
        assertThat(result.getName(), Is.is("new Candidate"));
    }
}
package ru.job4j.dream.store;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PsqlStoreTest {

    @Before
    public void reset() {
        Store store = PsqlStore.instOf();
        store.resetTable("post");
        store.resetTable("candidate");
        store.resetTable("city");
        store.resetTable("users");
    }

    @Test
    public void whenSavePost() {
        Store store = PsqlStore.instOf();
        Post post = new Post(0, "Java Job");
        store.save(post);
        post.setId(1);
        Post postInDb = store.findPostById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenFindAllPosts() {
        Store store = PsqlStore.instOf();
        Post post = new Post(0, "Java Job");
        Post post2 = new Post(0, "Java");
        store.save(post);
        store.save(post2);
        post.setId(1);
        post2.setId(2);
        List<Post> postsInDb = new ArrayList<>(store.findAllPosts());
        assertThat(postsInDb, is(List.of(post, post2)));
    }

    @Test
    public void whenSaveCandidateAndCityAndFindAllCandidates() {
        Store store = PsqlStore.instOf();
        City city = new City(0, "York");
        Candidate candidate = new Candidate(0, "Java Junior",
                city, Timestamp.valueOf(LocalDateTime.now()));
        store.save(city);
        store.save(candidate);
        city.setId(1);
        candidate.setId(1);
        City cityInDb = store.findCityByName(city.getName());
        Candidate candidateInDb = store.findCanById(candidate.getId());
        assertThat(cityInDb, is(city));
        assertThat(candidateInDb, is(candidate));
        assertThat(store.findAllCandidates(), is(List.of(candidate)));
    }

    @Test
    public void whenSaveUser() {
        Store store = PsqlStore.instOf();
        User user = new User(0, "Admin", "admin@mail.com", "12345");
        store.save(user);
        user.setId(1);
        User userInDb = store.findUserByEmail(user.getEmail());
        assertThat(userInDb, is(user));
    }

    @Test
    public void whenFindAllUsers() {
        Store store = PsqlStore.instOf();
        User user = new User(0, "Admin", "admin@mail.com", "12345");
        User user2 = new User(0, "Tom", "tomas@mail.com", "54321");
        store.save(user);
        store.save(user2);
        user.setId(1);
        user2.setId(2);
        List<User> usersInDb = new ArrayList<>(store.findAllUsers());
        assertThat(usersInDb, is(List.of(user, user2)));
    }
}
package ru.job4j.dream.store;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {

    private static final MemStore INST = new MemStore();

    private static final AtomicInteger POST_ID = new AtomicInteger(4);

    private static final AtomicInteger CANDIDATE_ID = new AtomicInteger(4);

    private static final AtomicInteger USER_ID = new AtomicInteger(2);

    private static final AtomicInteger CITY_ID = new AtomicInteger(4);

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private final Map<Integer, User> users = new ConcurrentHashMap<>();

    private final Map<Integer, City> cities = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", Timestamp.valueOf(LocalDateTime.now())));
        posts.put(2, new Post(2, "Middle Java Job", Timestamp.valueOf(LocalDateTime.now())));
        posts.put(3, new Post(3, "Senior Java Job", Timestamp.valueOf(LocalDateTime.now())));
        candidates.put(1, new Candidate(1, "Junior Java",
                new City(1, "York"), Timestamp.valueOf(LocalDateTime.now())));
        candidates.put(2, new Candidate(2, "Middle Java",
                new City(2, "Gorsk"), Timestamp.valueOf(LocalDateTime.now())));
        candidates.put(3, new Candidate(3, "Senior Java",
                new City(3, "Omsk"), Timestamp.valueOf(LocalDateTime.now())));
        users.put(1, new User(1, "Admin", "admin@mail.com", "12345"));
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public Collection<City> findAllCities() {
        return cities.values();
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            user.setId(USER_ID.incrementAndGet());
        }
        users.put(user.getId(), user);
    }

    @Override
    public void save(City city) {
        if (cities.containsValue(city.getName())) {
            return;
        }
        if (city.getId() == 0) {
            city.setId(USER_ID.incrementAndGet());
        }
        cities.put(city.getId(), city);
    }

    public Post findPostById(int id) {
        return posts.get(id);
    }

    public Candidate findCanById(int id) {
        return candidates.get(id);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = null;
        for (User value : users.values()) {
            if (value.getEmail().equals(email)) {
                user = value;
            }
        }
        return user;
    }

    @Override
    public City findCityByName(String name) {
        City city = null;
        for (City value : cities.values()) {
            if (value.getName().equals(name)) {
                city = value;
            }
        }
        return city;
    }

    @Override
    public Collection<Post> findPostsByDate(int days) {
        return null;
    }

    @Override
    public Collection<Candidate> findCandidatesByDate(int days) {
        return null;
    }
}

package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(PsqlStore.class.getClassLoader()
                        .getResourceAsStream("db.properties")))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name"),
                            it.getTimestamp("created")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "select can.id as id, can.name as name, city.id as cityId,"
                             + " city.name as cityName, can.created as date "
                         + "from candidate can left join city on can.city_id=city.id;")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(it.getInt("id"), it.getString("name"),
                            new City(it.getInt("cityId"), it.getString("cityName")),
                            it.getTimestamp("date")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return candidates;
    }

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    users.add(new User(it.getInt("id"), it.getString("name"),
                            it.getString("email"), it.getString("password")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return users;
    }

    @Override
    public Collection<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM city")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(new City(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return cities;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    @Override
    public void save(User user) {
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    @Override
    public void save(City city) {
        if (city.getId() == 0) {
            create(city);
        } else {
            update(city);
        }
    }

    private Post create(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO post(name, created) "
                             + "VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setTimestamp(2, post.getCreated());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return post;
    }

    private void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "update post set name=? where id=?;")
        ) {
            ps.setString(1, post.getName());
            ps.setInt(2, post.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
    }

    private Candidate create(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO candidate(name, city_id, created) VALUES (?,?,?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity().getId());
            ps.setTimestamp(3, candidate.getCreated());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return candidate;
    }

    private void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "update candidate set name=?, city_id=? where id=?;")
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getCity().getId());
            ps.setInt(3, candidate.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
    }

    private User create(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO users(name, email, password) VALUES (?,?,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return user;
    }

    private void update(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "update users set name=?, email=?, password=? where id=?;")
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
    }

    private City create(City city) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO city(name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, city.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    city.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
        return city;
    }

    private void update(City city) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "update city set name=? where id=?;")
        ) {
            ps.setString(1, city.getName());
            ps.setInt(2, city.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
    }

    @Override
    public Post findPostById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "select * from post where post.id=?;")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = new Post(rs.getInt("id"),
                            rs.getString("name"), rs.getTimestamp("created"));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        return post;
    }

    @Override
    public Candidate findCanById(int id) {
        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "select can.id as id, can.name as name, city.id as cityId,"
                             + " city.name as cityName, can.created as date "
                     + "from candidate can left join city on can.city_id=city.id where can.id=?;")
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate = new Candidate(rs.getInt("id"), rs.getString("name"),
                            new City(rs.getInt("cityId"), rs.getString("cityName")),
                            rs.getTimestamp("date"));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        return candidate;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "select * from users u where u.email=?;")
        ) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getInt("id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        return user;
    }

    @Override
    public City findCityByName(String name) {
        City city = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "select * from city c where c.name=?;")
        ) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    city = new City(rs.getInt("id"),
                            rs.getString("name"));
                    LOG.debug(city.getName());
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        return city;
    }

    @Override
    public Collection<Post> findPostsByDate(int days) {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "select * from post where created >= now() - cast(? as interval);")) {
            ps.setString(1, String.format("%s days", days));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(rs.getInt("id"), rs.getString("name"),
                            rs.getTimestamp("created")));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        LOG.info(String.valueOf(posts));
        return posts;
    }

    @Override
    public Collection<Candidate> findCandidatesByDate(int days) {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "select can.id as id, can.name as name, city.id as cityId, "
                             + "city.name as cityName, can.created as created "
                             + "from candidate can left join city on can.city_id=city.id "
                             + "where created >= now() - cast(? as interval);")) {
            ps.setString(1, String.format("%s days", days));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    candidates.add(new Candidate(rs.getInt("id"), rs.getString("name"),
                            new City(rs.getInt("cityId"), rs.getString("cityName")),
                            rs.getTimestamp("created")));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
        LOG.info(String.valueOf(candidates));
        return candidates;
    }

    @Override
    public void resetTable(String tableName) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("DELETE FROM " + tableName);
             PreparedStatement ps2 =  cn.prepareStatement(
                     "ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1")
        ) {
            ps.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception", e);
        }
    }
}

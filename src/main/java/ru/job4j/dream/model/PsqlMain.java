package ru.job4j.dream.model;

import ru.job4j.dream.store.PsqlStore;
import ru.job4j.dream.store.Store;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PsqlMain {

    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job", Timestamp.valueOf(LocalDateTime.now())));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        System.out.println("-------------------");
        store.save(new Candidate(0, "Java Junior",
                new City(30, "York"), Timestamp.valueOf(LocalDateTime.now())));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        System.out.println("-------------------");
        store.save(new Post(0, "Junior Java Job", Timestamp.valueOf(LocalDateTime.now())));
        System.out.println(store.findPostById(1));
        System.out.println("-------------------");
        store.save(new Candidate(1, "Middle Java",
                new City(31, "Gorsk"), Timestamp.valueOf(LocalDateTime.now())));
        System.out.println(store.findCanById(1));

    }
}

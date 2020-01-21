package com.bookstore.repository;

import com.bookstore.entity.Author;
import java.util.List;
import javax.persistence.QueryHint;
import static org.hibernate.jpa.QueryHints.HINT_PASS_DISTINCT_THROUGH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @QueryHints(value = @QueryHint(name = HINT_PASS_DISTINCT_THROUGH, value = "false"))
    @Query(value = "SELECT DISTINCT a FROM Author a JOIN FETCH a.books b WHERE a.age < ?1")
    List<Author> fetchAuthorsAndBooks(int age);
}


/*

Optimize SELECT DISTINCT Via Hibernate HINT_PASS_DISTINCT_THROUGH Hint

Description: Starting with Hibernate 5.2.2, we can optimize JPQL (HQL) query entites of type SELECT DISTINCT via HINT_PASS_DISTINCT_THROUGH hint. Keep in mind that this hint is useful only for JPQL (HQL) JOIN FETCH-ing queries. Is not useful for scalar queries (e.g., List<Integer>), DTO or HHH-13280. In such cases, the DISTINCT JPQL keyword is needed to be passed to the underlying SQL query. This will instruct the database to remove duplicates from the result set.

Key points:

use @QueryHints(value = @QueryHint(name = HINT_PASS_DISTINCT_THROUGH, value = "false"))
Output example:

*/
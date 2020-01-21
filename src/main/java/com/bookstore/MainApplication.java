package com.bookstore;

import com.bookstore.service.BookstoreService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final BookstoreService bookstoreService;

    public MainApplication(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            bookstoreService.batchAuthorsAndBooks();
            // bookstoreService.deleteAuthorsAndBooksViaDeleteAllInBatch();
            // bookstoreService.deleteAuthorsAndBooksViaDeleteInBatch();
            // bookstoreService.deleteAuthorsAndBooksViaDeleteAll();
            bookstoreService.deleteAuthorsAndBooksViaDelete();
        };
    }
}

/*
 * How To Batch Deletes In MySQL Via SQL ON DELETE CASCADE

Description: Batch deletes in MySQL via ON DELETE CASCADE. Auto-generated database schema will contain the ON DELETE CASCADE directive.

Note: Spring deleteAllInBatch() and deleteInBatch() don't use delete batching and don't take advantage of optimistic locking mechanism to prevent lost updates. They trigger bulk operations via Query.executeUpdate(), therefore, the Persistent Context is not synchronized accordingly (it is advisable to flush (before delete) and close/clear (after delete) the Persistent Context accordingly to avoid issues created by unflushed (if any) or outdated (if any) entities). The first one simply triggers a delete from entity_name statement, while the second one triggers a delete from entity_name where id=? or id=? or id=? ... statement. Both of them take advantage on ON DELETE CASCADE and are very efficient. For delete in batches rely on deleteAll(), deleteAll(Iterable<? extends T> entities) or delete() method. Behind the scene, the two flavors of deleteAll() relies on delete(). Mixing batching with database automatic actions (ON DELETE CASCADE) will result in a partially synchronized Persistent Context.

Key points:

in this application, we have a Author entity and each author can have several Book (one-to-many)
first, we remove orphanRemoval or set it to false
second, we use only CascadeType.PERSIST and CascadeType.MERGE
third, we set @OnDelete(action = OnDeleteAction.CASCADE) next to @OneToMany
fourth, we set spring.jpa.properties.hibernate.dialect to org.hibernate.dialect.MySQL5InnoDBDialect (or, MySQL8Dialect)
fifth, we run through each deleteFoo() method
Output example:


 * 
 */

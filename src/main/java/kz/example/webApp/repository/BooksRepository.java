package kz.example.webApp.repository;

import kz.example.webApp.entity.Book;
import kz.example.webApp.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
  List<Book> findByTitleStartingWith(String title);
}

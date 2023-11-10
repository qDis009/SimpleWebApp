package kz.example.webApp.service;

import kz.example.webApp.entity.Book;
import kz.example.webApp.entity.Person;
import kz.example.webApp.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BooksService {
  private final BooksRepository booksRepository;

  public List<Book> findAll(boolean sortByYear) {
    if (sortByYear) {
      return booksRepository.findAll(Sort.by("year"));
    }
    return booksRepository.findAll();
  }

  public List<Book> findAll(Integer page, Integer booksPerPage, boolean sortByYear) {
    if (sortByYear) {
      return booksRepository
          .findAll(PageRequest.of(page, booksPerPage, Sort.by("year")))
          .getContent();
    }
    return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
  }

  public List<Book> findBooksBySearchWord(String title) {
    return booksRepository.findByTitleStartingWith(title);
  }

  public Person findBookOwnerById(int id) {
    // Здесь hibernate.initialize() не нужен, так как владелец (сторона One) загружается лениво
    return booksRepository.findById(id).map(Book::getOwner).orElse(null);
  }

  public Book findById(int id) {
    Optional<Book> book = booksRepository.findById(id);
    return book.orElse(null);
  }

  @Transactional
  public void save(Book book) {
    booksRepository.save(book);
  }

  @Transactional
  public void delete(int id) {
    booksRepository.deleteById(id);
  }

  @Transactional
  public void update(int id, Book updatedBook) {
    updatedBook.setId(id);
    booksRepository.save(updatedBook);
  }

  // Освобождает книгу (когда человек возвращает книгу в библиотеку)
  @Transactional
  public void release(int id) {
    booksRepository
        .findById(id)
        .ifPresent(
            book -> {
              book.setOwner(null);
              book.setTakenAt(null);
            });
  }

  // Назначает книгу человеку (когда человек забирает книгу из библиотеки)
  @Transactional
  public void assign(int id, Person selectedPerson) {
    booksRepository
        .findById(id)
        .ifPresent(
            book -> {
              book.setOwner(selectedPerson);
              book.setTakenAt(new Date());
            });
  }
}

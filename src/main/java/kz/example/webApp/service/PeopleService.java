package kz.example.webApp.service;

import kz.example.webApp.entity.Book;
import kz.example.webApp.entity.Person;
import kz.example.webApp.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PeopleService {
  private final PeopleRepository peopleRepository;

  public List<Person> findAll() {
    return peopleRepository.findAll();
  }

  public Person findById(int id) {
    Optional<Person> person = peopleRepository.findById(id);
    return person.orElse(null);
  }

  @Transactional
  public void save(Person person) {
    peopleRepository.save(person);
  }

  @Transactional
  public void update(int id, Person updatedPerson) {
    updatedPerson.setId(id);
    peopleRepository.save(updatedPerson);
  }

  @Transactional
  public void delete(int id) {
    peopleRepository.deleteById(id);
  }

  public Optional<Person> getPersonByFullName(String fullName) {
    return peopleRepository.findByFullName(fullName);
  }

  public List<Book> getBooksByPersonId(int id) {
    Optional<Person> person = peopleRepository.findById(id);
    if (person.isPresent()) {
      Hibernate.initialize(person.get().getBooks());
      person
          .get()
          .getBooks()
          .forEach(
              book -> {
                long diffInMillis = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffInMillis > 864000000) {
                  book.setExpired(true);
                }
              });
      return person.get().getBooks();
    }
    return Collections.emptyList();
  }
}

package kz.example.webApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "year_of_birth")
  private int yearOfBirth;

  @OneToMany(mappedBy = "owner")
  private List<Book> books;
}

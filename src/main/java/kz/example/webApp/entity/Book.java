package kz.example.webApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "title")
  private String title;

  @Column(name = "author")
  private String author;

  @Column(name = "year")
  private int year;

  @ManyToOne
  @JoinColumn(name = "owner", referencedColumnName = "id")
  private Person owner;

  @Column(name = "taken_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date takenAt;

  @Transient
  private boolean expired;
}

package kz.example.webApp.controller;

import kz.example.webApp.entity.Book;
import kz.example.webApp.entity.Person;
import kz.example.webApp.service.BooksService;
import kz.example.webApp.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BooksController {
  private final BooksService booksService;
  private final PeopleService peopleService;

  @GetMapping
  public String books(
      Model model,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
      @RequestParam(value = "sort_by_year", required = false) boolean sortByYear) {
    if (page == null || booksPerPage == null) {
      model.addAttribute("books", booksService.findAll(sortByYear));
    } else {
      model.addAttribute("books", booksService.findAll(page, booksPerPage, sortByYear));
    }
    return "books/main";
  }

  @GetMapping("/{id}")
  public String getBook(
      Model model, @PathVariable("id") int id, @ModelAttribute("person") Person person) {
    model.addAttribute("book", booksService.findById(id));
    Person bookOwner = booksService.findBookOwnerById(id);
    if (bookOwner != null) {
      model.addAttribute("owner", bookOwner);
    } else {
      model.addAttribute("people", peopleService.findAll());
    }
    return "books/show";
  }

  @GetMapping("/new")
  public String newBook(@ModelAttribute("book") Book book) {
    return "books/new";
  }

  @PostMapping()
  public String createBook(@ModelAttribute("book") Book book, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "books/new";
    }
    booksService.save(book);
    return "redirect:/books";
  }

  @GetMapping("/{id}/edit")
  public String edit(Model model, @PathVariable("id") int id) {
    model.addAttribute("book", booksService.findById(id));
    return "books/edit";
  }

  @PatchMapping("/{id}")
  public String update(
      @ModelAttribute("book") Book book, BindingResult bindingResult, @PathVariable("id") int id) {
    if (bindingResult.hasErrors()) {
      return "books/edit";
    }
    booksService.update(id, book);
    return "redirect:/books";
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable("id") int id) {
    booksService.delete(id);
    return "redirect:/books";
  }

  @PatchMapping("/{id}/release")
  public String release(@PathVariable("id") int id) {
    booksService.release(id);
    return "redirect:/books/" + id;
  }

  @PatchMapping("/{id}/assign")
  public String assign(
      @PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson) {
    booksService.assign(id, selectedPerson);
    return "redirect:/books/" + id;
  }

  @GetMapping("/search")
  public String searchPage() {
    return "books/search";
  }

  @PostMapping("/search")
  public String makeSearch(Model model, @RequestParam("query") String query) {
    model.addAttribute("books", booksService.findBooksBySearchWord(query));
    return "books/search";
  }
}

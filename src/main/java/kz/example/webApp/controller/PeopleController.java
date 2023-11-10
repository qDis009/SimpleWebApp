package kz.example.webApp.controller;

import kz.example.webApp.entity.Person;
import kz.example.webApp.service.PeopleService;
import kz.example.webApp.util.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/people")
public class PeopleController {
  private final PeopleService peopleService;
  private final PersonValidator personValidator;

  @GetMapping
  public String people(Model model) {
    model.addAttribute("people", peopleService.findAll());
    return "people/main";
  }

  @GetMapping("/{id}")
  public String getPerson(Model model, @PathVariable("id") int id) {
    model.addAttribute("person", peopleService.findById(id));
    model.addAttribute("books", peopleService.getBooksByPersonId(id));
    return "people/show";
  }

  @GetMapping("/new")
  public String newPerson(@ModelAttribute("person") Person person) {
    return "people/new";
  }

  @PostMapping("/create")
  public String createPerson(@ModelAttribute("person") Person person, BindingResult bindingResult) {
    personValidator.validate(person, bindingResult);
    if (bindingResult.hasErrors()) {
      return "people/new";
    }
    peopleService.save(person);
    return "redirect:/people";
  }

  @GetMapping("/{id}/edit")
  public String editPerson(@PathVariable("id") int id, Model model) {
    model.addAttribute("person", peopleService.findById(id));
    return "people/edit";
  }

  @PatchMapping("/{id}")
  public String updatePerson(
      @PathVariable("id") int id, @ModelAttribute("person") @Validated Person updatedPerson) {
    peopleService.update(id, updatedPerson);
    return "redirect:/people";
  }

  @DeleteMapping("/delete/{id}")
  public String deletePerson(@PathVariable("id") int id) {
    peopleService.delete(id);
    return "redirect:/people";
  }
}

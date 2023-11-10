package kz.example.webApp.util;

import kz.example.webApp.entity.Person;
import kz.example.webApp.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {
    private final PeopleService peopleService;
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person=(Person) target;
        if(peopleService.getPersonByFullName(person.getFullName()).isPresent()){
            errors.rejectValue("fullName","","Человек с таким ФИО уже существует");
        }
    }
}

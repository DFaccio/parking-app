package br.com.gramado.parkingapp.service.person;

import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PersonServiceInterface {

    Person insert(Person person);

    Optional<Person> findById(String identifier);

    Page<Person> findAll(Pagination pagination);

    Person update(Person person);
}

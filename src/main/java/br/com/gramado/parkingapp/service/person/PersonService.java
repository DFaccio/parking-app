package br.com.gramado.parkingapp.service.person;

import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.repository.PersonRepository;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class PersonService implements PersonServiceInterface {

    private static final String SORT = "id";

    @Resource
    private PersonRepository repository;

    @Override
    public Person insert(Person person) {
        return repository.save(person);
    }

    @Override
    public Person update(Person person) {
        return repository.save(person);
    }

    @Override
    public Optional<Person> findByDocument(String document) {
        return repository.findPersonByDocumentEquals(document);
    }

    @Override
    public Page<Person> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(SORT));

        return repository.findAll(pageable);
    }
}

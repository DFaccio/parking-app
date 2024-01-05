package br.com.gramado.parkingapp.service.vehicle;

import br.com.gramado.parkingapp.entity.Person;
import br.com.gramado.parkingapp.entity.Vehicle;
import br.com.gramado.parkingapp.repository.VehicleRepository;
import br.com.gramado.parkingapp.service.person.PersonServiceInterface;
import br.com.gramado.parkingapp.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class VehicleService implements VehicleServiceInterface {

    private static final String SORT = "id";

    @Resource
    private VehicleRepository repository;

    @Resource
    private PersonServiceInterface personService;

    @Override
    public Page<Vehicle> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize(), Sort.by(SORT));

        return repository.findAll(pageable);
    }

    @Override
    public Vehicle insert(Vehicle vehicle) {
        Optional<Person> optional = personService.findById(vehicle.getPerson().getId());

        if (optional.isEmpty()) {
            Person person = personService.insert(vehicle.getPerson());
            vehicle.setPerson(person);
        }

        return repository.save(vehicle);
    }

    @Override
    public Vehicle update(Vehicle vehicle) {
        return repository.save(vehicle);
    }

    @Override
    public Optional<Vehicle> findByPersonIdAndPlate(String personId, String plate) {
        return repository.findVehicleByPlateEqualsAndPerson_Id(plate, personId);
    }
}

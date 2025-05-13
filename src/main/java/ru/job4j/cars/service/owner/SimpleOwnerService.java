package ru.job4j.cars.service.owner;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.repository.OwnerRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleOwnerService implements OwnerService {

	private final OwnerRepository ownerRepository;

	@Override
	public Owner save(Owner owner) {
		return ownerRepository.save(owner);
	}

	@Override
	public void deleteByIdList(List<Integer> ownerIds) {
		ownerRepository.deleteByOwnerIdList(ownerIds);
	}

	@Override
	public Optional<Owner> findByUserName(String name) {
		return ownerRepository.findByUserName(name);
	}

}

package org.bian.exc.carcatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.bian.exc.carcatalog.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}

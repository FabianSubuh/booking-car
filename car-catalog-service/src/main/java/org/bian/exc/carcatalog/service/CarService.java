package org.bian.exc.carcatalog.service;

import jakarta.transaction.Transactional;
import org.bian.exc.carcatalog.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bian.exc.carcatalog.entity.Car;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Creates a new car model.
     * @param car The car object to create.
     * @return The created car object.
     */
    @Transactional
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    /**
     * Retrieves all car models.
     * @return A list of all car models.
     */
    @Transactional
//            (value = Transactional.TxType.)
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    /**
     * Retrieves a car model by its ID.
     * @param id The ID of the car model.
     * @return An Optional containing the car if found, otherwise empty.
     */
    @Transactional
//            (readOnly = true)
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    /**
     * Updates an existing car model.
     * @param id The ID of the car model to update.
     * @param updatedCar The car object with updated details.
     * @return The updated car object.
     * @throws RuntimeException if the car is not found.
     */
    @Transactional
    public Car updateCar(Long id, Car updatedCar) {
        return carRepository.findById(id).map(car -> {
            car.setModelName(updatedCar.getModelName());
            car.setBrand(updatedCar.getBrand());
            car.setProdYear(updatedCar.getProdYear());
            car.setColor(updatedCar.getColor());
            car.setFeatures(updatedCar.getFeatures());
            car.setImageUrl(updatedCar.getImageUrl());
            return carRepository.save(car);
        }).orElseThrow(() -> new RuntimeException("Car not found with id " + id));
    }

    /**
     * Deletes a car model by its ID.
     * @param id The ID of the car model to delete.
     */
    @Transactional
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }

    /**
     * Initial Data
     */
    @Transactional
    public void populateInitialData() {
        if (carRepository.count() == 0) {
            carRepository.save(new Car("Creta", "Hyundai", 2024, "White", "Sunroof, SmartSense", "https://example.com/images/creta.jpg"));
            carRepository.save(new Car("IONIQ 5", "Hyundai", 2023, "Black", "EV, Fast Charging", "https://example.com/images/ioniq5.jpg"));
            carRepository.save(new Car("Palisade", "Hyundai", 2024, "Grey", "7-Seater, AWD", "https://example.com/images/palisade.jpg"));
            System.out.println("Car Catalog Service: Initial data populated.");
        }
    }
}
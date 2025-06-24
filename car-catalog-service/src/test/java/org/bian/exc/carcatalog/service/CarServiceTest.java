package org.bian.exc.carcatalog.service;

import org.bian.exc.carcatalog.entity.Car;
import org.bian.exc.carcatalog.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car testCar;

    @BeforeEach
    void setUp() {
        testCar = new Car("Venue", "Hyundai", 2023, "Red", "Compact SUV", "https://example.com/images/venue.jpg");
        testCar.setId(1L);
    }

    @Test
    void testCreateCar() {
        when(carRepository.save(any(Car.class))).thenReturn(testCar);
        Car createdCar = carService.createCar(new Car("Venue", "Hyundai", 2023, "Red", "Compact SUV", "https://example.com/images/venue.jpg"));
        assertNotNull(createdCar);
        assertEquals("Venue", createdCar.getModelName());
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void testGetAllCars() {
        when(carRepository.findAll()).thenReturn(Arrays.asList(testCar, new Car("Elantra", "Hyundai", 2024, "Blue", "Sedan", "https://example.com/images/elantra.jpg")));
        List<Car> cars = carService.getAllCars();
        assertNotNull(cars);
        assertEquals(2, cars.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testGetCarByIdFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));
        Optional<Car> foundCar = carService.getCarById(1L);
        assertTrue(foundCar.isPresent());
        assertEquals("Venue", foundCar.get().getModelName());
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCarByIdNotFound() {
        when(carRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<Car> foundCar = carService.getCarById(2L);
        assertFalse(foundCar.isPresent());
        verify(carRepository, times(1)).findById(2L);
    }

    @Test
    void testUpdateCar() {
        Car updatedDetails = new Car("Venue", "Hyundai", 2024, "Black", "Compact SUV, Updated", "https://example.com/images/venue_new.jpg");
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));
        when(carRepository.save(any(Car.class))).thenReturn(updatedDetails);

        Car result = carService.updateCar(1L, updatedDetails);
        assertNotNull(result);
        assertEquals(2024, result.getProdYear());
        assertEquals("Black", result.getColor());
        assertEquals("https://example.com/images/venue_new.jpg", result.getImageUrl());
        verify(carRepository, times(1)).findById(1L);
        verify(carRepository, times(1)).save(testCar);
    }

    @Test
    void testUpdateCarNotFound() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());
        Car updatedDetails = new Car("Venue", "Hyundai", 2024, "Black", "Compact SUV, Updated", "https://example.com/images/venue_new.jpg");
        assertThrows(RuntimeException.class, () -> carService.updateCar(99L, updatedDetails));
        verify(carRepository, times(1)).findById(99L);
        verify(carRepository, never()).save(any(Car.class));
    }

    @Test
    void testDeleteCar() {
        doNothing().when(carRepository).deleteById(1L);
        carService.deleteCar(1L);
        verify(carRepository, times(1)).deleteById(1L);
    }
}

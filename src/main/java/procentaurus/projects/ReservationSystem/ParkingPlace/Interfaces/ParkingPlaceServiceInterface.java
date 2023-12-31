package procentaurus.projects.ReservationSystem.ParkingPlace.Interfaces;

import procentaurus.projects.ReservationSystem.Exceptions.NonExistingParkingPlaceException;
import procentaurus.projects.ReservationSystem.ParkingPlace.Dtos.ParkingPlaceUpdateDto;
import procentaurus.projects.ReservationSystem.ParkingPlace.ParkingPlace;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ParkingPlaceServiceInterface {

    Optional<ParkingPlace> findSingleParkingPlace(int number);

    List<ParkingPlace> findParkingPlaces(Map<String, String> params);

    List<ParkingPlace> findAvailableParkingPlaces(LocalDate startDate, int numberOfDays, ParkingPlace.VehicleType vehicleType)
            throws NonExistingParkingPlaceException;

    boolean deleteParkingPlace(int number);

    Optional<ParkingPlace> updateParkingPlace(int number, ParkingPlaceUpdateDto parkingPlace);

    Optional<ParkingPlace> createParkingPlace(ParkingPlace parkingPlace);
}

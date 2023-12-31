package procentaurus.projects.ReservationSystem.Room.Interfaces;

import procentaurus.projects.ReservationSystem.Exceptions.NonExistingRoomException;
import procentaurus.projects.ReservationSystem.Room.Dtos.RoomUpdateDto;
import procentaurus.projects.ReservationSystem.Room.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RoomServiceInterface {
    Optional<Room> findSingleRoom(int number);

    List<Room> findRooms(Map<String, String> params);

    List<Room> findAvailableRooms(LocalDate startDate, short numberOfDays, Room.RoomType standard, Boolean viewForLake, Boolean forSmokingPeople)
            throws NonExistingRoomException;

    boolean deleteRoom(int number);

    Optional<Room> updateRoom(int number, RoomUpdateDto room);

    Optional<Room> createRoom(Room room);
}

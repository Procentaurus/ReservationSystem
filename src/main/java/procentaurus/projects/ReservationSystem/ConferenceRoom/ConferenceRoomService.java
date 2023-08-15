package procentaurus.projects.ReservationSystem.ConferenceRoom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import procentaurus.projects.ReservationSystem.ConferenceRoom.Interfaces.ConferenceRoomRepository;
import procentaurus.projects.ReservationSystem.ConferenceRoom.Interfaces.ConferenceRoomServiceInterface;
import procentaurus.projects.ReservationSystem.Exceptions.NonExistingConferenceRoomException;
import procentaurus.projects.ReservationSystem.Slot.Interfaces.SlotRepository;
import procentaurus.projects.ReservationSystem.Slot.Slot;
import procentaurus.projects.ReservationSystem.Space.Space;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static procentaurus.projects.ReservationSystem.ConferenceRoom.ConferenceRoomFilter.filterByHasStage;
import static procentaurus.projects.ReservationSystem.Miscellaneous.FilterPossibilityChecker.checkIfDateIsInPeriod;
import static procentaurus.projects.ReservationSystem.Space.SpaceFilter.*;


@Service
public class ConferenceRoomService implements ConferenceRoomServiceInterface {

    private final ConferenceRoomRepository conferenceRoomRepository;
    private final SlotRepository slotRepository;

    @Autowired
    public ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository, SlotRepository slotRepository) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public Optional<ConferenceRoom> findSingleConferenceRoom(int number) {
        return conferenceRoomRepository.findByNumber(number);
    }

    @Override
    public List<ConferenceRoom> findConferenceRooms(Map<String, String> params) {
        List<ConferenceRoom> all = conferenceRoomRepository.findAll();

        if(params != null) {
            if (params.containsKey("capacity"))
                if(isFilteringByCapacityPossible(params.get("price")))
                    all = filterByCapacity(all.stream().map(x ->(Space) x).toList(),
                            Integer.parseInt(params.get("price").substring(2)),    // value passed
                            params.get("price").substring(0, 2),                              // mark passed, one from [==, <=, >=]
                            ConferenceRoom.class);

            if (params.containsKey("price"))
                if(isFilteringByPricePossible(params.get("price")))
                    all = filterByPrice(all.stream().map(x ->(Space) x).toList(),
                            Float.parseFloat(params.get("price").substring(2)),    // value passed
                            params.get("price").substring(0, 2),                              // mark passed, one from [==, <=, >=]
                            ConferenceRoom.class);

            if (params.containsKey("hasStage")) all = filterByHasStage(all, params.get("hasStage"));
        }
        return all;
    }

    @Override
    public List<ConferenceRoom> findAvailableConferenceRooms(LocalDate startDate, int numberOfDays, boolean hasStage) throws NonExistingConferenceRoomException {

        ArrayList<ConferenceRoom> toReturn = new ArrayList<>();
        List<Slot> data = slotRepository.findByParkingPlaceIsNotNull();

        // Group slots by room ID
        Map<Integer, List<Slot>> slotsByRoomId = data.stream().collect(Collectors.groupingBy(slot -> slot.getParkingPlace().getNumber()));

        for (Map.Entry<Integer, List<Slot>> entry : slotsByRoomId.entrySet()) {

            List<Slot> slotsInChosenPeriod = entry.getValue().stream().
                    filter(slot -> checkIfDateIsInPeriod(startDate, slot.getDate(), numberOfDays)).toList();

            boolean success = true;
            for (Slot slot : slotsInChosenPeriod) {
                if(slot.getConferenceRoom().getHasStage() != hasStage) success = false;
                if(slot.getStatus().equals(Slot.Status.FREE)) success = false;
                if(!success) break;
            }

            Optional<ConferenceRoom> toAdd = conferenceRoomRepository.findByNumber(entry.getKey());
            if(toAdd.isPresent()) toReturn.add(toAdd.get());
            else throw new NonExistingConferenceRoomException(entry.getKey());
        }
        return toReturn;
    }

    @Override
    public boolean deleteConferenceRoom(int number) {
        if(conferenceRoomRepository.existsByNumber(number)){
            conferenceRoomRepository.deleteByNumber(number);
            return true;
        }else return false;
    }

    @Override
    public Optional<ConferenceRoom> updateConferenceRoom(int number, ConferenceRoom conferenceRoom) {
        Optional<ConferenceRoom> toUpdate = conferenceRoomRepository.findByNumber(number);
        if(toUpdate.isPresent() && conferenceRoom != null){

            float price = conferenceRoom.getPrice();
            int capacity = conferenceRoom.getCapacity();
            int numberToChange = conferenceRoom.getNumber();
            Boolean hasStage = conferenceRoom.getHasStage();
            String description = conferenceRoom.getDescription();

            try {
                if(price != 0.0) toUpdate.get().setPrice(price);
                if(capacity != 0) toUpdate.get().setCapacity(capacity);
                if(numberToChange != 0) toUpdate.get().setNumber(number);
                if(hasStage != null) toUpdate.get().setHasStage(hasStage);
                if(description != null) toUpdate.get().setDescription(description);

                conferenceRoomRepository.save(toUpdate.get());
                return toUpdate;

            } catch (DataAccessException ex) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ConferenceRoom> createConferenceRoom(ConferenceRoom conferenceRoom) {
        try {
            ConferenceRoom created = conferenceRoomRepository.save(conferenceRoom);
            return Optional.of(created);
        }catch(IllegalArgumentException ex){
            return Optional.empty();
        }
    }
}

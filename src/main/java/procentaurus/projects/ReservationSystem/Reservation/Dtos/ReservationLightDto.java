package procentaurus.projects.ReservationSystem.Reservation.Dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import procentaurus.projects.ReservationSystem.Guest.Guest;
import procentaurus.projects.ReservationSystem.Reservation.Reservation;
import procentaurus.projects.ReservationSystem.Slot.Dtos.SlotLightDto;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ReservationLightDto {

    @FutureOrPresent
    private final LocalDate startDate;

    @Size( min = 1, max = 90)
    private final short numberOfDays;

    private final Set<Long> guestsIds;
    private final Set<String> guestsEmails;
    private final Set<SlotLightDto> occupiedSpaces;


    public ReservationLightDto(Reservation reservation, boolean useEmails) {

        if (useEmails) {
            this.guestsEmails = reservation.getGuests().stream().map(Guest::getEmail).collect(Collectors.toSet());
            this.guestsIds = null;
        } else {
            this.guestsIds = reservation.getGuests().stream().map(Guest::getId).collect(Collectors.toSet());
            this.guestsEmails = null;
        }

        this.startDate = reservation.getStartDate();
        this.numberOfDays = reservation.getNumberOfDays();
        this.occupiedSpaces = reservation.getOccupiedSlots().stream().map(SlotLightDto::new).collect(Collectors.toSet());
    }
}

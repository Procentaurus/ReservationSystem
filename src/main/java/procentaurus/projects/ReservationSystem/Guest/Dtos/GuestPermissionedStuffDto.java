package procentaurus.projects.ReservationSystem.Guest.Dtos;

import lombok.Getter;
import lombok.Setter;
import procentaurus.projects.ReservationSystem.Guest.Guest;
import procentaurus.projects.ReservationSystem.Guest.TroubleCaused.TroubleCausedByGuest;

import java.util.List;

@Getter
@Setter
public class GuestPermissionedStuffDto extends GuestBasicDto {

    private List<TroubleCausedByGuest> troubleCaused;

    public GuestPermissionedStuffDto(Guest guest) {
        super(guest.getFirstName(), guest.getLastName(), guest.getDateOfBirth(), guest.getPhoneNumber(), guest.getEmail(), guest.isSignedForNewsletter());
        //this.troubleCaused = guest.getTroubleCa9used();
    }
}

package procentaurus.projects.ReservationSystem.MyUser.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class MyUserUpdateDto {

    protected String firstName;
    protected String lastName;
    protected LocalDate dateOfBirth;
    protected Integer phoneNumber;
    protected String email;

    public boolean isValid(){
        if(firstName != null && (firstName.length() < 3 || firstName.length() > 20)) return false;
        if(lastName != null && (lastName.length() < 3 || lastName.length() > 30)) return false;
        if(dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now())) return false;
        if(phoneNumber != null && (phoneNumber > 999999999 || phoneNumber < 100000000)) return false;
        if(email != null && (!email.contains("@") || email.length() < 7 || !email.contains("."))) return false;

        return true;
    }
}

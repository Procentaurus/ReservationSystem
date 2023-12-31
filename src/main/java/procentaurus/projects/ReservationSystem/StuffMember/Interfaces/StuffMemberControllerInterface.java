package procentaurus.projects.ReservationSystem.StuffMember.Interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import procentaurus.projects.ReservationSystem.StuffMember.Dtos.StuffMemberCreationDto;
import procentaurus.projects.ReservationSystem.StuffMember.Dtos.StuffMemberUpdateDto;
import procentaurus.projects.ReservationSystem.StuffMember.StuffMember;

import java.util.List;
import java.util.Map;

public interface StuffMemberControllerInterface {

    ResponseEntity<?> findSingleStuffMember(Long id, UserDetails userDetails);

    ResponseEntity<List<StuffMember>> findStuffMembers(Map<String, String> params);

    ResponseEntity<?> deleteStuffMember(Long id);

    ResponseEntity<?> updateStuffMember(Long id, StuffMemberUpdateDto stuffMember, UserDetails userDetails);
}

package tn.esprit.clubconnect.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.clubconnect.entities.Club;
import tn.esprit.clubconnect.entities.Role;
import tn.esprit.clubconnect.entities.User;
import tn.esprit.clubconnect.repositories.IClubRepository;
import tn.esprit.clubconnect.repositories.IUserRepository;
import tn.esprit.clubconnect.requestAndresponse.RoleChangeRequest;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService{

    private final IUserRepository userRepository;
    private final IClubRepository clubRepository;

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public void deleteUser(int id) {
       userRepository.deleteById(id);
    }

    @Override
    public void changeUserRole(int id, RoleChangeRequest request) {
        User user = userRepository.findByIdU(id);
        user.setRole(request.getRole());
    }

    @Override
    public void lockUser(int id) {
        User user = userRepository.findByIdU(id);
        user.setLocked(true);


    }

    @Override
    public List<Club> getAllClubs() {
        return (List<Club>) clubRepository.findAll();
    }


}

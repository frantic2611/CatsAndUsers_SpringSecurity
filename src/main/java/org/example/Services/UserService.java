package org.example.Services;

import org.example.DAO.UserDAO;
import org.example.Entities.Cat;
import org.example.Entities.User;
import org.example.EntitiesDTO.UserDTO;
import org.example.Mapper.UserDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService{
    private UserDAO userDAO;
    private UserDTOMapper userDTOMapper;
    private PasswordEncoder passwordEncoder;

    private CatService catService;

    @Autowired
    public UserService(UserDAO userDAO, UserDTOMapper userDTOMapper, PasswordEncoder passwordEncoder, CatService catService){
        this.userDAO = userDAO;
        this.userDTOMapper = userDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.catService = catService;
    }
    public void save(User entity) {
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        userDAO.save(entity);
    }

    public List<UserDTO> getAll() {
        return userDAO.findAll()
                .stream()
                .map(userDTOMapper)
                .collect(Collectors.toList());
    }
    public void delete(int id) {
        if(userDAO.findById(id).get().getCats() != null){
            for( Cat cat : userDAO.findById(id).get().getCats()){
                catService.delete(cat.getId());
            }
        }
        userDAO.deleteById(id);
    }

    public UserDTO getById(int id) {
        return userDAO.findById(id)
                .map(userDTOMapper)
                .orElseThrow();
    }

    public Optional<User> findByUsername(String username){
        return userDAO.findByUsername(username);
    }

}

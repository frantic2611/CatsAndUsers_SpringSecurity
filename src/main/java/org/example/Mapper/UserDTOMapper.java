package org.example.Mapper;

import org.example.Entities.User;
import org.example.EntitiesDTO.UserDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User human){
        return new UserDTO(
                human.getId(),
                human.getName(),
                human.getUsername(),
                human.getRole(),
                human.getPassword(),
                human.getBirth(),
                human.getCats()
                        .stream()
                        .map(r -> r.getCat().getId())
                        .collect(Collectors.toList())
        );
    }
}

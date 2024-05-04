package org.example.Controllers;

import org.example.Entities.Span.UserSpan;
import org.example.Entities.User;
import org.example.EntitiesDTO.UserDTO;
import org.example.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService humanService){
        this.userService = humanService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> saveHuman(@RequestBody UserSpan userSpan){
        User user = User.builder()
                .birth(userSpan.getBirth())
                .username(userSpan.getUsername())
                .password(userSpan.getPassword())
                .role(userSpan.getRole())
                .name(userSpan.getName())
                .build();
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllHumans(){
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public ResponseEntity<UserDTO> getHuman(@PathVariable(name = "id") int id){
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public HttpStatus deleteHuman(@PathVariable(name = "id") int id){
        userService.delete(id);
        return HttpStatus.OK;
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateHuman(@PathVariable(name = "id") int id, @RequestBody UserSpan userSpan){
        User human = User.builder()
                .role(userSpan.getRole())
                .password(userSpan.getPassword())
                .username(userSpan.getUsername())
                .birth(userSpan.getBirth())
                .name(userSpan.getName()).build();
        human.setId(id);
        userService.save(human);
        return new ResponseEntity<>(human, HttpStatus.OK);
    }
}

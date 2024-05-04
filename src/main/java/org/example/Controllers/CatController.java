package org.example.Controllers;


import org.example.Config.MyUserDetails;
import org.example.DAO.UserDAO;
import org.example.Entities.Cat;
import org.example.Entities.Span.CatSpan;
import org.example.Entities.User;
import org.example.EntitiesDTO.CatDTO;
import org.example.Services.CatService;
import org.example.Services.MyDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cats")
public class CatController {
    private final CatService catService;


    @Autowired
    public CatController(CatService catService, UserDAO userDAO){
        this.catService = catService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Cat> saveCat(@RequestBody CatSpan catSpan){
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        catSpan.setOwnerId(myUserDetails.getId());
        return new ResponseEntity<>(catService.save(catSpan), HttpStatus.OK);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CatDTO>> getAllCats(){
        return new ResponseEntity<>(catService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or principal.id == @catService.getById(#id)")
    public ResponseEntity<CatDTO> getCat(@PathVariable(name = "id") int id){
        return new ResponseEntity<>(catService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public HttpStatus deleteCat(@PathVariable("id") Integer id){
            var auth = SecurityContextHolder.getContext().getAuthentication();

        if(((MyUserDetails)auth.getPrincipal()).getAuthority().equals("ROLE_USER")){
            if(((MyUserDetails) auth.getPrincipal()).getId() == catService.getOwnerId(id)){
                catService.delete(id);
                return HttpStatus.OK;
            }
        }
        else {
            catService.delete(id);
            return HttpStatus.OK;
        }
        return HttpStatus.EXPECTATION_FAILED;
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Cat> updateCat(@PathVariable(name = "id") int id,@RequestBody CatSpan catSpan){
        return new ResponseEntity<>(catService.update(id, catSpan), HttpStatus.OK);
    }

    //Дополнительные фильтры
    /////////////////////////////////////////////////////////////////////////////////


    @GetMapping("/color/{color}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER') ")
    public ResponseEntity<List<CatDTO>> findByColor(@PathVariable (name = "color") String color){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<CatDTO> cats = null;
        if("ROLE_USER".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            cats = catService.findByColor(color).stream().filter(x -> x.getOwner() == ((MyUserDetails) auth
                    .getPrincipal()).getId()).collect(Collectors.toList());
        }
        else {
            cats = catService.findByColor(color);
        }
        return new ResponseEntity<>(cats, HttpStatus.OK);
    }

    @GetMapping("/breed/{breed}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER') ")
    public ResponseEntity<List<CatDTO>> findByBreed(@PathVariable (name = "breed") String breed){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<CatDTO> cats = null;
        if("ROLE_USER".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            cats = catService.findByBreed(breed).stream().filter(x -> x.getOwner() == ((MyUserDetails) auth
                    .getPrincipal()).getId()).collect(Collectors.toList());
        }
        else {
            cats = catService.findByBreed(breed);
        }
        return new ResponseEntity<>(cats, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<List<CatDTO>> findByName(@PathVariable (name = "name") String name){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        List<CatDTO> cats = null;
        if("ROLE_USER".equals(((MyUserDetails)auth.getPrincipal()).getAuthority())){
            cats = catService.findByName(name).stream().filter(x -> x.getOwner() == ((MyUserDetails) auth
                    .getPrincipal()).getId()).collect(Collectors.toList());
        }
        else {
            cats = catService.findByName(name);
        }
        return new ResponseEntity<>(cats, HttpStatus.OK);
    }

    @GetMapping("/owner/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<CatDTO>> findBOwnerId(@PathVariable (name = "id") int id){
        if(catService.findByOwnerId(id) == null || catService.findByOwnerId(id).isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(catService.findByOwnerId(id), HttpStatus.OK);
    }
}
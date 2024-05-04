package org.example.Services;

import org.example.DAO.CatDAO;
import org.example.DAO.UserDAO;
import org.example.Entities.Cat;
import org.example.Entities.Span.CatSpan;
import org.example.Entities.User;
import org.example.EntitiesDTO.CatDTO;
import org.example.EntitiesDTO.UserDTO;
import org.example.Mapper.CatDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CatService{
    private final CatDAO catDAO;
    private final CatDTOMapper catDTOMapper;
    private final UserDAO userDAO;

    @Autowired
    public CatService(CatDAO catDAO, CatDTOMapper catDTOMapper, UserDAO userDAO){
        this.catDAO = catDAO;
        this.catDTOMapper = catDTOMapper;
        this.userDAO = userDAO;
    }
    public Cat save(CatSpan catSpan){
        Cat cat = Cat.builder()
                .breed(catSpan.getBreed())
                .owner(userDAO.findById(catSpan.getOwnerId()).get())
                .color(catSpan.getColor())
                .birth(catSpan.getBirth())
                .name(catSpan.getName()).build();
        if(catSpan.getFriends() != null){

            for(int friendId : catSpan.getFriends()) {
                Cat friend = catDAO.findById(friendId).get();
                cat.add(friend);
            }
        }
        catDAO.save(cat);
        if(cat.getFriends() != null){
            for(Cat friendId : cat.getFriends()) {
                friendId.add(cat);
            }
            catDAO.saveAll(cat.getFriends());
        }
        return catDAO.save(cat);
    }

    public Cat update(int id, CatSpan catSpan){
        Cat cat = catDAO.findById(id).get();
        cat.setBirth(catSpan.getBirth());
        cat.setBreed(catSpan.getBreed());
        cat.setName(catSpan.getName());
        cat.setColor(catSpan.getColor());

        if(catSpan.getFriends() != null){
            for(int friendId : catSpan.getFriends()) {
                Cat friend = catDAO.findById(friendId).get();
                cat.add(friend);
            }
        }
        catDAO.save(cat);
        if(cat.getFriends() != null){
            for(Cat friend : cat.getFriends())
                if(!friend.getFriends().contains(cat)) friend.add(cat);
            catDAO.saveAll(cat.getFriends());
        }
        return catDAO.save(cat);
    }
    public List<CatDTO> getAll(){
        return catDAO.findAll()
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }
    public void delete(int id){
        if(catDAO.findById(id).get().getFriends() != null){
            for( Cat friend : catDAO.findById(id).get().getFriends()){
                friend.getCat().deleteFriend(id);
            }
        }
        catDAO.deleteById(id);
    }
    public CatDTO getById(int id){
        return catDAO.findById(id)
                .map(catDTOMapper)
                .orElseThrow();
    }
    public List<CatDTO> findByColor(String color){
        return catDAO.findByColor(color)
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }
    public List<CatDTO> findByBreed(String breed){
        return catDAO.findByBreed(breed)
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }
    public List<CatDTO> findByName(String name){
        return catDAO.findByName(name)
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }
    public List<CatDTO> findByOwnerId(Integer id){
        return catDAO.findByOwnerId(id)
                .stream()
                .map(catDTOMapper)
                .collect(Collectors.toList());
    }

    public int getOwnerId(int id){
        return catDAO.findById(id).get().getOwner().getId();
    }
}

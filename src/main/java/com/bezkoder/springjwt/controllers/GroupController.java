package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Group;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.GroupRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class GroupController  {


    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/group")
    public ResponseEntity<Group> createGroup(@RequestBody Group group, Principal principal) {
        try {
            Set<User> userSet = new HashSet<>();
            Optional<User> user = userRepository.findByUsername(principal.getName());
            userSet.add(user.get());
            Group group2 = new Group(group.getGroupName(), principal.getName());
            group2.setUsers(userSet);

            Group group1 = groupRepository.save(group2);
            return new ResponseEntity<>(group1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/groups")
//    public ResponseEntity<List<Group>> getAllGroups() {
//
//
//
//        List<Group> groupList = groupRepository.findAll();
//
//
//
//        return new ResponseEntity<>(groupList,HttpStatus.OK);
//    }


     @GetMapping("/groups")
      public ResponseEntity<List<Group>> getGroupsByWhoCreated(Principal principal) {
         Optional<User> user  = userRepository.findByUsername(principal.getName());

         List<Group> groupList = groupRepository.findByCreatedBy(user.get().getUsername());

         return new ResponseEntity<>(groupList,HttpStatus.OK);

    }

    @GetMapping("/group/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable("id") Long id) {
        Optional<Group> group = groupRepository.findById(id);
        Group group1  = group.get();
        return new ResponseEntity<>(group1,HttpStatus.OK);

    }

    @DeleteMapping("/group/{id}")
    public ResponseEntity<Group> deleteByGroupById(@PathVariable("id") Long id) {
          groupRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }


}

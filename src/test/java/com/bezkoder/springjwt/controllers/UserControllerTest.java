package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAllUsers() {
        // mock the repository to return a list of users
        User user1 = new User( "Doe", "john.doe@example.com", "password");
        User user2 = new User( "Doe", "jane.doe@example.com", "password");
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // call the controller method
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());

        // verify that the repository method was called once
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUser() {
        // mock the repository to return a user with the given ID
        Long id = 1L;
        User user1 = new User( "Doe", "john.doe@example.com", "password");
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));

        // call the controller method
        ResponseEntity<User> response = userController.getUser(id);

        // verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user1, response.getBody());

        // verify that the repository method was called once with the given ID
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testCreateUser() {
        // mock the repository to return the created user
        User user = new User( "Doe", "john.doe@example.com", "password");
        when(userRepository.save(user)).thenReturn(user);

        // mock a HttpServletRequest to simulate the request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // call the controller method
        ResponseEntity<User> response = userController.createUser(user);

        // verify the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());

        // verify that the repository method was called once with the given user
        verify(userRepository, times(1)).save(user);

        // verify the location header in the response
        URI expectedLocation = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        assertEquals(expectedLocation, response.getHeaders().getLocation());

        // clear the request context
        RequestContextHolder.resetRequestAttributes();
    }


    @Test
    public void testUpdateUser() {
        // mock the repository to return the updated user
        Long id = 1L;
        User user = new User( "Doe", "john.doe@example.com", "password");
        when(userRepository.save(user)).thenReturn(user);

        // call the controller method
        ResponseEntity<User> response = userController.updateUser(user);

        // verify the response status and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        // verify that the repository method was called once with the given user
        verify(userRepository, times(1)).save(user);
    }
}

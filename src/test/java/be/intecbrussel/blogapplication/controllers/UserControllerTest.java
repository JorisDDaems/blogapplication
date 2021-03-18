package be.intecbrussel.blogapplication.controllers;

import be.intecbrussel.blogapplication.web_security_config.WebConfig;

import be.intecbrussel.blogapplication.model.User;
import be.intecbrussel.blogapplication.services.UserService;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
class UserControllerTest {

    @InjectMocks
    WebConfig webConfig;

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    User user;

    Principal mockPrincipal;

    @Mock
    JButton follow;


    @BeforeEach
    void setUp() {
        mockPrincipal = mock(Principal.class);
        when(mockPrincipal.getName()).thenReturn("abc@gmail.com");

        MockitoAnnotations.openMocks(this);
        user = User.builder().id(1L).email("abc@gmail.com").password("abcdef").build();
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setViewResolvers(webConfig.viewResolver())
                .build();
    }

    @Test
    void updateProfile() throws Exception {

        when(userService.findById(1L)).thenReturn(user);
        when(userService.findByEmail(any())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/" + user.getId() +"/edit")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("user/updateProfile"))
                .andExpect(model().attributeExists("user"));

    }

    @Test
    void processUpdateProfile() throws Exception {

        mockMvc.perform(post("/user/" + user.getId() +"/edit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/" + user.getId() + "/profile"));
    }

    @Test
    void showProfile() throws Exception {

        when(userService.findById(1L)).thenReturn(user);
        when(userService.findByEmail(any())).thenReturn(user);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/user/1/profile")
                .principal(mockPrincipal);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"));

    }

    @Test
    void followUser() throws Exception{

        User nonLoggedInUser = new User();
        List<User> listOfFollowers = new ArrayList<>();
        listOfFollowers.add(user);
        nonLoggedInUser.setId(1L);

        when(userService.findByEmail(any())).thenReturn(nonLoggedInUser);
        assertNotEquals(nonLoggedInUser, user);

        Optional<User> userOptional1 = Optional.of(user);

        userOptional1.ifPresent(x->x.setFollowers(listOfFollowers));

        assertEquals(listOfFollowers.get(0), user);

    }

    @Test
    void unfollowUser() throws Exception{
        User nonLoggedInUser = new User();
        List<User> listOfFollowers = new ArrayList<>();
        listOfFollowers.add(user);
        nonLoggedInUser.setId(1L);

        when(userService.findByEmail(any())).thenReturn(nonLoggedInUser);
        assertEquals(listOfFollowers.get(0), user);

        listOfFollowers.remove(user);
        assertEquals(listOfFollowers.contains(user), eq(false));



    }



}
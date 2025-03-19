package com.suraj.UserService.services.impl;


import com.suraj.UserService.entities.User;
import com.suraj.UserService.exceptions.ResourceNotFoundException;
import com.suraj.UserService.external.service.HotelService;
import com.suraj.UserService.repositories.UserRepository;
import com.suraj.UserService.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.converters.Auto;
import com.suraj.UserService.entities.*;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
     
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        //generate  unique userid
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        //implement RATING SERVICE CALL: USING REST TEMPLATE
        return userRepository.findAll();
    }

    //get single user
    @Override
    public User getUser(String userId) {
        //get user from database with the help  of user repository
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server !! : " + userId));
        // fetch rating of the above  user from RATING SERVICE
        //http://localhost:8083/ratings/users/47e38dac-c7d0-4c40-8582-11d15f185fad
       
        Rating [] ratingOfUser = restTemplate.getForObject("http://RATINGSERVICE/ratings/users/" + user.getUserId(),
                Rating[].class);
               List<Rating> ratings =Arrays.stream(ratingOfUser).toList();

               List<Rating> ratingList = ratings.stream().map(rating -> {
                // by using RestTemplate 

                // Hotel hotel = restTemplate.getForObject("http://HOTELSERVICE/hotels/"+rating.getHotelId(),Hotel.class);

                // By using FeignClient
                
                Hotel hotel= hotelService.getHotel(rating.getHotelId());

                rating.setHotel(hotel);
                return rating;

               }).collect(Collectors.toList());
               user.setRatings(ratingList);

        return user;
    }
}

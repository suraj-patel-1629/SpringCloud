package com.suraj.UserService.external.service;
import com.suraj.UserService.entities.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="HOTELSERVICE")
public interface HotelService {
    
  @GetMapping("hotels/{hotelId}")
  Hotel getHotel(@PathVariable String hotelId);
}

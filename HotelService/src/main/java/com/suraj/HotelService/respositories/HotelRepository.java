package com.suraj.HotelService.respositories;


import com.suraj.HotelService.entites.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, String> {
}

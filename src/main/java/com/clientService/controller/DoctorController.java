package com.clientService.controller;

import com.clientService.Service.DoctorService;
import com.clientService.model.DoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @GetMapping("/doctor/{doctorId}/profile") // Corrected URL mapping
    public String getDoctorProfile(@PathVariable("doctorId") int id, Model model){
                DoctorDTO doctor = new DoctorDTO();
                doctor.setDateOfJoining(LocalDate.now()); // Assuming `doctor.dateOfJoining` is of type `LocalDate`
                model.addAttribute("doctor", doctor);
                return "doctor-profile";  // Thymeleaf template
            }
}

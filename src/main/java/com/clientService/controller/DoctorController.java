package com.clientService.controller;

import com.clientService.Service.DoctorService;
  // Assuming you have a PatientDTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DoctorController {

    @Autowired
    DoctorService doctorService;

//    @Autowired
//    PatientService patientService;  // Assuming you have a PatientService for fetching patient data

    @GetMapping("/doctor-profile")
    public String getDoctorProfile(
            @RequestParam("doctorId") int doctorId,
            @RequestParam("patientId") int patientId,
            Model model) {

        // Fetch doctor data based on doctorId
//        DoctorDTO doctor = doctorService.getDoctorById(doctorId);
//
        // Fetch patient data based on patientId
//        PatientDTO patient = patientService.getPatientById(patientId);
//
//        // Handle cases where doctor or patient is not found
//        if (doctor == null) {
//            model.addAttribute("error", "Doctor not found");
//            return "error-page";  // Return an error page template
//        }
//
//        if (patient == null) {
//            model.addAttribute("error", "Patient not found");
//            return "error-page";  // Return an error page template
//        }
//
//        // Add doctor and patient data to the model
//        model.addAttribute("doctor", doctor);
//        model.addAttribute("patient", patient);

        // Return the doctor-profile template for rendering
        return "doctor-profile"; // Thymeleaf template to display the doctor and patient information
    }
}

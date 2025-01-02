package com.clientService.controller;

import com.clientService.model.AppointmentDTO;
import com.clientService.model.CancelAppointmentDTO;
import com.clientService.model.DoctorDTO;
import com.clientService.model.ScheduleAppointmentDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/patient")
public class AppointmentController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{patientId}/appointments")
    public String showAppointmentsForPatient(@PathVariable Long patientId, Model model) {
        String url = "http://localhost:8080/patient/" + patientId + "/appointments";
        try {
            ResponseEntity<List<AppointmentDTO>> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<AppointmentDTO>>() {}
            );
            List<AppointmentDTO> appointments = response.getBody();

            if (appointments == null || appointments.isEmpty()) {
                model.addAttribute("message", "No appointments found.");
            } else {
            	model.addAttribute("patientId", patientId);
                model.addAttribute("appointments", appointments);
            }
            return "appointments";
        } catch (ResourceAccessException e) {
            model.addAttribute("errorMessage", "Unable to fetch appointments. Please check the backend service.");
            return "error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred while fetching appointments.");
            return "error";
        }
    }

    @GetMapping("/{patientId}/appointments/schedule")
    public String showScheduleAppointment(@PathVariable Long patientId, Model model) {
    	String url = "http://localhost:8080/api/doctors";
        try {
            // Make GET request to fetch doctors
            ResponseEntity<List<DoctorDTO>> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<DoctorDTO>>() {}
            );
            
            // Get the list of doctors from the response
            List<DoctorDTO> doctors = response.getBody();

            // Add attributes to the model
            model.addAttribute("scheduleAppointmentDTO", new ScheduleAppointmentDTO());
            model.addAttribute("patientId", patientId);
            model.addAttribute("doctors", doctors); // Add the list of doctors to the model

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to fetch doctors. Please try again.");
            return "error";
        }
        
        // Return the view
        return "appointment-schedule";
    }

    @PostMapping("/{patientId}/appointments/schedule")
    public String scheduleAppointment(@PathVariable Long patientId, 
                                       @ModelAttribute ScheduleAppointmentDTO scheduleAppointmentDTO, 
                                       Model model) {
        String url = "http://localhost:8080/patient/" + patientId + "/appointments/schedule";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<ScheduleAppointmentDTO> request = new HttpEntity<>(scheduleAppointmentDTO, headers);

            ResponseEntity<AppointmentDTO> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                AppointmentDTO.class
            );

            AppointmentDTO createdAppointment = response.getBody();

            if (createdAppointment != null) {
            	model.addAttribute("patientId", patientId);
                model.addAttribute("appointment", createdAppointment);
                return "appointment-confirmation";
            } else {
                model.addAttribute("errorMessage", "Appointment could not be scheduled. Please try again.");
                return "error";
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("errorMessage", "Invalid input or scheduling conflict. Please check your details.");
            return "error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to schedule the appointment. Please try again.");
            return "error";
        }
    }

    @GetMapping("/{patientId}/appointments/cancel")
    public String showCancelAppointment(@PathVariable Long patientId, Model model) {
        model.addAttribute("cancelAppointmentDTO", new CancelAppointmentDTO());
        model.addAttribute("patientId", patientId);
        return "appointment-cancel";
    }

    @PostMapping("/{patientId}/appointments/cancel")
    public String cancelAppointment(@PathVariable Long patientId, 
                                     @ModelAttribute CancelAppointmentDTO cancelAppointmentDTO, 
                                     Model model) {
        String url = "http://localhost:8080/patient/" + patientId + "/appointments/cancel";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<CancelAppointmentDTO> request = new HttpEntity<>(cancelAppointmentDTO, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                String.class
            );

            String cancelledAppointment = response.getBody();

            if (cancelledAppointment != null) {
                model.addAttribute("appointment", cancelledAppointment);
                return "appointment-cancel-confirmation";
            } else {
                model.addAttribute("errorMessage", "Appointment could not be cancelled. Please try again.");
                return "error";
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("errorMessage", "Invalid request or cancellation error. Please check the details.");
            return "error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to cancel the appointment. Please try again.");
            return "error";
        }
    }
}

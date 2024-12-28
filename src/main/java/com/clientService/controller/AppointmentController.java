package com.clientService.controller;

import com.clientService.model.AppointmentDTO;
import com.clientService.model.CancelAppointmentDTO;
import com.clientService.model.ScheduleAppointmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/patient")
public class AppointmentController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{patientId}/appointments")
    public String showAppointmentsForPatient(@PathVariable Long patientId, Model model) {
        try {
            String url = "http://localhost:8080/patient/" + patientId + "/appointments";
            AppointmentDTO[] appointments = restTemplate.getForObject(url, AppointmentDTO[].class);

            if (appointments == null || appointments.length == 0) {
                model.addAttribute("message", "No appointments found.");
            } else {
                model.addAttribute("appointments", appointments);
            }

            return "appointments"; // Thymeleaf template for displaying appointments
        } catch (ResourceAccessException e) {
            model.addAttribute("errorMessage", "Unable to fetch appointments. Please check the backend service.");
            return "error"; // Error page template
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred while fetching appointments.");
            return "error";
        }
    }

    @GetMapping("/{patientId}/appointments/schedule")
    public String showScheduleAppointment(@PathVariable Long patientId, Model model) {
        model.addAttribute("scheduleAppointmentDTO", new ScheduleAppointmentDTO());
        model.addAttribute("patientId", patientId);
        return "appointment-schedule";
    }

    @PostMapping("/{patientId}/appointments/schedule")
    public String scheduleAppointment(@PathVariable Long patientId, @ModelAttribute ScheduleAppointmentDTO scheduleAppointmentDTO, Model model) {
        try {
            String url = "http://localhost:8080/patient/" + patientId + "/appointments/schedule";
            AppointmentDTO createdAppointment = restTemplate.postForObject(url, scheduleAppointmentDTO, AppointmentDTO.class);

            if (createdAppointment != null) {
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
    public String cancelAppointment(@PathVariable Long patientId, @ModelAttribute CancelAppointmentDTO cancelAppointmentDTO, Model model) {
        try {
            String url = "http://localhost:8080/patient/" + patientId + "/appointments/cancel";
            AppointmentDTO cancelledAppointment = restTemplate.postForObject(url, cancelAppointmentDTO, AppointmentDTO.class);

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


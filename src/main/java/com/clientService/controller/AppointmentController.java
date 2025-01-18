package com.clientService.controller;

import com.clientService.model.AppointmentDTO;
import com.clientService.model.CancelAppointmentDTO;
import com.clientService.model.ScheduleAppointmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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

    // View all appointments for a specific patient
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

    // Show the page to schedule a new appointment
    @GetMapping("/{patientId}/appointments/schedule")
    public String showScheduleAppointment(@PathVariable Long patientId, Model model) {
        model.addAttribute("scheduleAppointmentDTO", new ScheduleAppointmentDTO());
        model.addAttribute("patientId", patientId);
        return "appointment-schedule";
    }

    // Schedule an appointment
    @PostMapping("/{patientId}/appointments/schedule")
    public String scheduleAppointment(
            @PathVariable Long patientId,
            @RequestBody ScheduleAppointmentDTO scheduleAppointmentDTO,
            Model model) {

        try {
            String url = "http://localhost:8080/patient/" + patientId + "/appointments/schedule";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ScheduleAppointmentDTO> requestEntity = new HttpEntity<>(scheduleAppointmentDTO, headers);

            AppointmentDTO createdAppointment = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AppointmentDTO.class).getBody();

            if (createdAppointment != null) {
                model.addAttribute("appointment", createdAppointment);
                return "appointment-confirmation"; // Show confirmation view
            } else {
                model.addAttribute("errorMessage", "Appointment could not be scheduled. Please try again.");
                return "error";  // Show error page
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("errorMessage", "Invalid input or scheduling conflict. Please check your details.");
            return "error";  // Show error page with message
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to schedule the appointment. Please try again.");
            return "error";  // Show error page for other errors
        }
    }

    // Show the page to cancel an appointment
    @GetMapping("/{patientId}/appointments/cancel/{id}")
    public String showCancelAppointment(@PathVariable Long patientId, @PathVariable Long id, Model model) {
        model.addAttribute("cancelAppointmentDTO", new CancelAppointmentDTO());
        model.addAttribute("patientId", patientId);
        model.addAttribute("appointmentId", id); // Add appointment ID for context
        return "appointment-cancel";
    }

    // Cancel an appointment
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

    // View the profile of a doctor
    @GetMapping("/doctor/{doctorId}/profile")
    public String getDoctorProfile(@PathVariable("doctorId") Long doctorId, Model model) {
        // Logic to fetch doctor details (if required)
        return "doctor-profile"; // This will render the doctor-profile.html page
    }

    // View the page to reschedule an appointment
    @GetMapping("/{patientId}/appointments/reschedule/{appointmentId}")
    public String getReschedulePage(@PathVariable Long patientId, @PathVariable Long appointmentId) {
        System.out.println("Navigating to reschedule page for Patient ID: " + patientId + ", Appointment ID: " + appointmentId);
        return "reschedule-appointment"; // Ensure the HTML file is named reschedule-appointment.html
    }
}

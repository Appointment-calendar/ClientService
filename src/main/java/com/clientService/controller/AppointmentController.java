package com.clientService.controller;

import com.clientService.model.AppointmentDTO;
import com.clientService.model.CancelAppointmentDTO;
import com.clientService.model.Doctor;
import com.clientService.model.ScheduleAppointmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    public String scheduleAppointment(
            @PathVariable Long patientId,
            @RequestBody ScheduleAppointmentDTO scheduleAppointmentDTO,
            Model model) {

        try {
            // Construct the API endpoint for scheduling the appointment
            String url = "http://localhost:8080/patient/" + patientId + "/appointments/schedule";

            // Set headers for the request
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Wrap the DTO in an HttpEntity to include headers and body
            HttpEntity<ScheduleAppointmentDTO> requestEntity = new HttpEntity<>(scheduleAppointmentDTO, headers);

            // Send the POST request and get the response as AppointmentDTO
            AppointmentDTO createdAppointment = restTemplate.exchange(url, HttpMethod.POST, requestEntity, AppointmentDTO.class).getBody();

            // Check if appointment was created successfully
            if (createdAppointment != null) {
                model.addAttribute("appointment", createdAppointment);
                return "appointment-confirmation";  // Show confirmation view
            } else {
                model.addAttribute("errorMessage", "Appointment could not be scheduled. Please try again.");
                return "error";  // Show error page if appointment creation failed
            }
        } catch (HttpClientErrorException e) {
            // Handle HTTP client error (e.g., 400 Bad Request)
            model.addAttribute("errorMessage", "Invalid input or scheduling conflict. Please check your details.");
            return "error";  // Show error page with message
        } catch (Exception e) {
            // Handle other general exceptions
            model.addAttribute("errorMessage", "Failed to schedule the appointment. Please try again.");
            return "error";  // Show error page for other errors
        }
    }
    @GetMapping("/{patientId}/appointments/cancel/{id}")
    public String showCancelAppointment(@PathVariable Long patientId, @PathVariable Long id, Model model) {
        model.addAttribute("cancelAppointmentDTO", new CancelAppointmentDTO());
        model.addAttribute("patientId", patientId);
        model.addAttribute("appointmentId", id); // Add if needed for context
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
    @GetMapping("/doctor/{doctorId}/profile")
    public String getDoctorProfile(@PathVariable("doctorId") Long doctorId, Model model) {

        return "doctor-profile"; // This will render the doctor-profile.html page
    }
    @GetMapping("/{patientId}/appointments/reschedule/{appointmentId}")
    public String getReschedulePage(@PathVariable Long patientId, @PathVariable Long appointmentId) {
        // Log patient and appointment details for debugging
        System.out.println("Navigating to reschedule page for Patient ID: " + patientId + ", Appointment ID: " + appointmentId);

        // Return the view name for rendering the reschedule page
        return "reschedule-appointment"; // Ensure the HTML file is named reschedule-appointment.html
    }

}


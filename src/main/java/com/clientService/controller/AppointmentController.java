package com.clientService.controller;

import com.clientService.model.AppointmentDTO; // DTO for appointments
import com.clientService.model.ScheduleAppointmentDTO; // DTO for scheduling an appointment
import com.clientService.model.CancelAppointmentDTO; // DTO for canceling an appointment
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/patient")
public class AppointmentController {

    @Autowired
    private RestTemplate restTemplate;

    
    @GetMapping("/{patientId}/appointments")
    public String showAppointmentsForPatient(@PathVariable Long patientId, Model model) {
        try {
           
            String url = "http://localhost:8080/patient/" + patientId + "/appointments"; // Updated URL
            AppointmentDTO[] appointments = restTemplate.getForObject(url, AppointmentDTO[].class);

            if (appointments == null || appointments.length == 0) {
                model.addAttribute("message", "No appointments found.");
            } else {
                model.addAttribute("appointments", appointments);
            }

            return "appointments"; 
        } catch (ResourceAccessException e) {
            model.addAttribute("errorMessage", "Unable to fetch appointments. Please check the backend service.");
            return "error"; // Return an error page if the backend service is unavailable
        }
    }

    
    @GetMapping("/{patientId}/appointments/schedule")
    public String showScheduleAppointment(@PathVariable Long patientId, Model model) {
        return "appointment-schedule"; 
    }

    // Endpoint to handle scheduling a new appointment
    @PostMapping("/{patientId}/appointments/schedule")
    public String scheduleAppointment(@PathVariable int patientId, ScheduleAppointmentDTO scheduleAppointmentDTO, Model model) {
        String url = "http://localhost:8080/patient/" + patientId + "/appointments/schedule"; 
        AppointmentDTO createdAppointment = restTemplate.postForObject(url, scheduleAppointmentDTO, AppointmentDTO.class);

        
        model.addAttribute("appointment", createdAppointment);
        return "appointment-confirmation"; 
    }

    
    @GetMapping("/{patientId}/appointments/cancel")
    public String showCancelAppointment(@PathVariable Long patientId, Model model) {
        return "appointment-cancel"; 
    }

   
    @PostMapping("/{patientId}/appointments/cancel")
    public String cancelAppointment(@PathVariable Long patientId, @RequestParam("appointmentId") Long appointmentId, Model model) {
        // Send POST request to backend to cancel the appointment
        String url = "http://localhost:8080/patient/" + patientId + "/appointments/cancel"; // Updated URL
        CancelAppointmentDTO cancelAppointmentDTO = new CancelAppointmentDTO(appointmentId);
        restTemplate.postForObject(url, cancelAppointmentDTO, Void.class);

        // Add a message to the model to show that the appointment was canceled
        model.addAttribute("message", "Appointment canceled successfully.");
        return "appointment-cancel-confirmation"; // Thymeleaf template for cancel confirmation
    }
    

}

























// package com.clientService.controller;

// import com.clientService.model.AppointmentDTO; // DTO for appointments
// import com.clientService.model.ScheduleAppointmentDTO; // DTO for scheduling an appointment
// import com.clientService.model.CancelAppointmentDTO; // DTO for canceling an appointment
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.bind.annotation.RequestMapping;

// @Controller
// @RequestMapping("/appointments")
// public class AppointmentController {

//     @Autowired
//     private RestTemplate restTemplate;

//     // Endpoint to show all appointments
//     @GetMapping
//     public String showAppointmentManagement(Model model) {
//         // Fetch all appointments from backend (port 8000)
//         String url = "http://localhost:8000/appointments";
        
//         // Use DTO instead of backend model
//         AppointmentDTO[] appointments = restTemplate.getForObject(url, AppointmentDTO[].class);
        
//         // Add appointments to the model
//         model.addAttribute("appointments", appointments);
//         return "appointment"; // Thymeleaf template
//     }

//     // Endpoint to show the schedule appointment form
//     @GetMapping("/schedule")
//     public String showScheduleAppointment(Model model) {
//         return "appointment-schedule"; // Thymeleaf template for scheduling
//     }

//     // Endpoint to handle scheduling a new appointment
//     @PostMapping("/schedule")
//     public String scheduleAppointment(ScheduleAppointmentDTO scheduleAppointmentDTO, Model model) {
//         // Send POST request to backend to schedule the appointment
//         String url = "http://localhost:8000/appointments/schedule";
//         AppointmentDTO createdAppointment = restTemplate.postForObject(url, scheduleAppointmentDTO, AppointmentDTO.class);

//         // Add the created appointment to the model for confirmation
//         model.addAttribute("appointment", createdAppointment);
//         return "appointment-confirmation"; // Thymeleaf template for confirmation
//     }

//     // Endpoint to show the cancel appointment form
//     @GetMapping("/cancel")
//     public String showCancelAppointment(Model model) {
//         return "appointment-cancel"; // Thymeleaf template for canceling
//     }

//     // Endpoint to handle canceling an appointment
//     @PostMapping("/cancel")
//     public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId, Model model) {
//         // Send POST request to backend to cancel the appointment
//         String url = "http://localhost:8000/appointments/cancel";
//         CancelAppointmentDTO cancelAppointmentDTO = new CancelAppointmentDTO(appointmentId);
//         restTemplate.postForObject(url, cancelAppointmentDTO, Void.class);

//         // Add a message to the model to show that the appointment was canceled
//         model.addAttribute("message", "Appointment canceled successfully.");
//         return "appointment-cancel-confirmation"; // Thymeleaf template for cancel confirmation
//     }
// }























// package com.clientService.controller;

// import com.clientService.model.AppointmentDTO; // Use client-side DTO
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.client.RestTemplate;  // Import RestTemplate
// import org.springframework.web.bind.annotation.RequestMapping;


// @Controller
// @RequestMapping("/appointments") // Make sure this is here
// public class HomeController {

//     @Autowired
//     private RestTemplate restTemplate;

//     @GetMapping
//     public String showAppointmentManagement(Model model) {
//         // Fetch all appointments from backend (port 8000)
//         String url = "http://localhost:8000/appointments";
        
//         // Use DTO instead of backend model
//         AppointmentDTO[] appointments = restTemplate.getForObject(url, AppointmentDTO[].class);
        
//         // Add appointments to the model for Thymeleaf
//         model.addAttribute("appointments", appointments);
//         return "appointment"; // Thymeleaf template
//     }

//     @GetMapping("/schedule")
//     public String showScheduleAppointment(Model model) {
//         return "appointment-schedule"; // Thymeleaf template
//     }

//     @GetMapping("/cancel")
//     public String showCancelAppointment() {
//         return "appointment-cancel"; // Thymeleaf template
//     }
// }


















// package com.clientService.controller;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

// @Controller
// @RequestMapping("/appointments")
// public class HomeController {
	
// 	@GetMapping
//    public String showAppointmentManagement() {
//        return "appointment"; 
//    }

  
// //    @GetMapping("/schedule")
// //    public String showScheduleAppointment(Model model) {

// //                                model.addAttribute("doctors", doctorService.getAllDoctors());
// //        return "appointment-schedule"; 
// //    }

  
//    @GetMapping("/cancel")
//    public String showCancelAppointment() {
//        return "appointment-cancel"; 
//    }

   
// //    @PostMapping("/create")
// //    public String createAppointment(Appointment appointment) {
// //        appointmentService.createAppointment(appointment);
// //        return "redirect:/appointments/manage"; 
// //    }
// //
// //    @PostMapping("/cancel")
// //    public String cancelAppointment(String appointmentId, String cancelReason) {
// //        appointmentService.cancelAppointment(appointmentId, cancelReason);
// //        return "redirect:/appointments/manage"; 
// //    }
// }


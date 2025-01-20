package com.clientService.Service;

import com.clientService.model.DoctorDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Optional;

@Service
public class DoctorService {

    private final RestTemplate restTemplate;

    public DoctorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<DoctorDTO> getDoctorById(int doctorId) {
        String apiUrl = "http://localhost:8080/api/doctors/" + doctorId;

        try {
            DoctorDTO doctorDTO = restTemplate.getForObject(apiUrl, DoctorDTO.class);
            return Optional.ofNullable(doctorDTO);
        } catch (HttpClientErrorException e) {
            // Log error details here and handle it according to your needs
            return Optional.empty(); // Returning Optional empty if doctor is not found
        }
    }
}

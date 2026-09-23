package org.example.medorabackend.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/patients")

public class PatientController {
@GetMapping
    public String getPatients(){
        return "Patient endpoint works";
    }
}

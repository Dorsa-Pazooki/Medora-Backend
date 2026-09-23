package org.example.medorabackend.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController {
    @RequestMapping("/api/appointments")
    public String getAppointment(){
        return "Appointment endpoints works!";
    }
}

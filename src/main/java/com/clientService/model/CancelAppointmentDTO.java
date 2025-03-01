package com.clientService.model;

public class CancelAppointmentDTO {
    private Long appointmentId;
    private String reasonOfCancellation;

    public CancelAppointmentDTO(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getReasonOfCancellation() {
        return reasonOfCancellation;
    }

    public CancelAppointmentDTO() {
    }

    public void setReasonOfCancellation(String reasonOfCancellation) {
        this.reasonOfCancellation = reasonOfCancellation;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public CancelAppointmentDTO(Long appointmentId, String reasonOfCancellation) {
        this.appointmentId = appointmentId;
        this.reasonOfCancellation = reasonOfCancellation;
    }
    

}

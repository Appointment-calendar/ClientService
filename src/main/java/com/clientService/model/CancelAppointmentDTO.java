package com.clientService.model;

public class CancelAppointmentDTO {
    private Integer appointmentId;
    private String reasonOfCancellation;
    
	public CancelAppointmentDTO() {
		//TODO
	}

	public CancelAppointmentDTO(Integer appointmentId, String reasonOfCancellation) {
		this.appointmentId = appointmentId;
		this.reasonOfCancellation = reasonOfCancellation;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getReasonOfCancellation() {
		return reasonOfCancellation;
	}

	public void setReasonOfCancellation(String reasonOfCancellation) {
		this.reasonOfCancellation = reasonOfCancellation;
	}

}

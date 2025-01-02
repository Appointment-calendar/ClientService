package com.clientService.model;

public class CancelAppointmentDTO {
    private int appointmentId;
    private String reasonOfCancellation;
    
	public CancelAppointmentDTO() {
		//TODO
	}

	public CancelAppointmentDTO(int appointmentId, String reasonOfCancellation) {
		this.appointmentId = appointmentId;
		this.reasonOfCancellation = reasonOfCancellation;
	}

	public int getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getReasonOfCancellation() {
		return reasonOfCancellation;
	}

	public void setReasonOfCancellation(String reasonOfCancellation) {
		this.reasonOfCancellation = reasonOfCancellation;
	}

    

    
    

}

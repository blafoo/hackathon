package de.hdi.portal.backend;

public class UserInfo {
	
	private String personalnummer;
	
	private String pin;
	
	public UserInfo(String personalnummer, String pin) {
		this.personalnummer = personalnummer;
		this.pin = pin;
	}

	public String getPersonalnummer() {
		return personalnummer;
	}

	public void setPersonalnummer(String personalnummer) {
		this.personalnummer = personalnummer;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
	

}

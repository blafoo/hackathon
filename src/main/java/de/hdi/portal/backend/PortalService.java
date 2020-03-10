package de.hdi.portal.backend;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import de.hdi.portal.utils.VaadinHelper;

@Service
public class PortalService {
	
	private static final String HDI_PIN = "HDI_PIN";

	private static final String HDI_PERSONALNUMMER = "HDI_PERSONALNUMMER";

	public boolean isRegistrationValid(UserInfo user) {
		return user.getPin() != null;
	}
	
	/**
	 * Infos lokal speichern
	 * 
	 * @param personalnummer
	 * @param pin
	 */
	public void save(UserInfo user) {
		
		VaadinHelper.setCookie(HDI_PERSONALNUMMER, user.getPersonalnummer(), "Personalnummer");
		
		VaadinHelper.setCookie(HDI_PIN, user.getPin(), "Pin");
	}
	
	/**
	 * UserInfos aus Cookie holen
	 * 
	 * @return
	 */
	public UserInfo load() {
		
		Optional<Cookie> personalnummer = VaadinHelper.getCookieByName(HDI_PERSONALNUMMER);
		if ( personalnummer.isPresent() ) {
			Optional<Cookie> pin = VaadinHelper.getCookieByName(HDI_PIN);
			return new UserInfo(personalnummer.get().getValue(), pin.isPresent() ? pin.get().getValue() : null);
		}
		
		return new UserInfo(null, null);
	}
	
	public String kommen(UserInfo user) {
		return String.format("Kommen gebucht um %s", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
	}
	
	public String gehen(UserInfo user) {
		return String.format("Gehen gebucht um %s", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
	}

}

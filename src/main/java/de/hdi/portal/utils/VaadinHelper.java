package de.hdi.portal.utils;

import java.util.Optional;

import javax.servlet.http.Cookie;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinService;

public class VaadinHelper {

	public static void showNotification(String text, NotificationVariant variant) {
		Notification notification = new Notification(text);
		if ( variant != null ) {
			notification.addThemeVariants(variant);
		}
		notification.setDuration(5000);
		notification.open();
	}
	
	public static void setCookie(String name, String value, String comment) {
		Cookie cookie = new Cookie(name, value);
		cookie.setComment(comment);
		cookie.setMaxAge(60*60*24*7*12); // 12 Wochen
		cookie.setPath(VaadinService.getCurrentRequest().getContextPath());

		VaadinService.getCurrentResponse().addCookie(cookie);
	}
	
	public static Optional<Cookie> getCookieByName(String name) {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		
		if ( cookies == null ) {
			return Optional.empty();
		}

		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return Optional.of(cookie);
			}
		}

		return Optional.empty();
	}

}

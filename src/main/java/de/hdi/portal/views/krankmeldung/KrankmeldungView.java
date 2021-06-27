package de.hdi.portal.views.krankmeldung;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.hdi.portal.backend.PortalService;
import de.hdi.portal.backend.UserInfo;
import de.hdi.portal.utils.VaadinHelper;
import de.hdi.portal.views.main.MainView;
import de.hdi.portal.views.zeiterfassung.RegistrierungView;

@Route(value = "krank", layout = MainView.class)
@PageTitle("Krankmeldung")
@CssImport("styles/views/hdi/hdi-view.css")
public class KrankmeldungView extends Div {

	private static final long serialVersionUID = -3999057735999935719L;

	@Autowired
	private PortalService service;

	private UserInfo user;

	public KrankmeldungView() {

		setId("zeit-view");
		VerticalLayout layout = createWrapper();

		createTitle(layout);
		createButtonLayout(layout);

		add(layout);
	}

	@PostConstruct
	public void init() {
		UserInfo user = service.load();
		if ( !service.isRegistrationValid(user) ) {
			UI.getCurrent().navigate(RegistrierungView.class);
			UI.getCurrent().getPage().reload();
		}
	}

	private VerticalLayout createWrapper() {
		VerticalLayout wrapper = new VerticalLayout();
		wrapper.setId("wrapper");
		wrapper.setSpacing(false);
		return wrapper;
	}

	private void createTitle(VerticalLayout layout) {
		H1 h1 = new H1("Krankmeldung");
		layout.add(h1);
	}

	private void createButtonLayout(VerticalLayout layout) {

		// TODO Buttons horizontal mittig ausrichten
		// Layout/Icon aus Mitarbeiter Portal übernehmen

		MemoryBuffer buffer = new MemoryBuffer();
		Upload upload = new Upload(buffer);

		upload.setMaxFiles(1);
		upload.setDropLabel(new Label("Max. 5MB PDF/JPG/PNG hochladen"));
		upload.setAcceptedFileTypes("application/pdf", "image/jpeg", "image/png");
		upload.setMaxFileSize(5*1024*1024); // 5 MB
		upload.setUploadButton(new Button("Krankmeldung hochladen"));
		upload.setAutoUpload(true);

		upload.addFileRejectedListener(event -> {
			VaadinHelper.showNotification(event.getErrorMessage(), NotificationVariant.LUMO_ERROR);
		});

		upload.addSucceededListener(event -> {
			Dialog dialog = new Dialog();
			dialog.add(new Paragraph("MIME Type: " + event.getMIMEType()));
			dialog.add(new Paragraph("Dateiname: " + event.getFileName()));
			dialog.add(new Paragraph("Dateigröße: " + readInputStream(buffer.getInputStream())));
			dialog.open();
			Notification.show(service.krankmeldung(user, buffer.getInputStream()));
		});


		layout.add(upload);
	}

	private int readInputStream(InputStream is) {
		int size = 0;
		try (Reader reader = new BufferedReader(new InputStreamReader(is))) {
			int c = 0;
			while ((c = reader.read()) != -1) {
				// textBuilder.append((char) c);
				size++;
			}
		} catch (IOException e) {
			return -1;
		}
		return size;
	}

}

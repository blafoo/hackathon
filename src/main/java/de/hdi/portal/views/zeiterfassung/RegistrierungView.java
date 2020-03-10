package de.hdi.portal.views.zeiterfassung;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.FormItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.hdi.portal.backend.PortalService;
import de.hdi.portal.backend.UserInfo;
import de.hdi.portal.utils.VaadinHelper;
import de.hdi.portal.views.main.MainView;

@Route(value = "register", layout = MainView.class)
@PageTitle("Registrierung")
@CssImport("styles/views/hdi/hdi-view.css")
public class RegistrierungView extends Div {

	private static final long serialVersionUID = -3999057735999935719L;
	
	private TextField personalnummer = new TextField();
    private TextField pin = new TextField();

    @Autowired
	private PortalService service;
	
    public RegistrierungView() {
        
        setId("register-view");
        VerticalLayout wrapper = createWrapper();

        createTitle(wrapper);
        createFormLayout(wrapper);
        createButtonLayout(wrapper);

        add(wrapper);
    }
    
    @PostConstruct
    public void init() {
    	UserInfo user = service.load();
    	personalnummer.setValue(user.getPersonalnummer() != null ? user.getPersonalnummer() : "");
    	pin.setValue(user.getPin() != null ? user.getPin() : "");
    }

    private void createTitle(VerticalLayout wrapper) {
        H1 h1 = new H1("Registrierung");
        wrapper.add(h1);
    }

    private VerticalLayout createWrapper() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setId("wrapper");
        wrapper.setSpacing(false);
        return wrapper;
    }

    private void createFormLayout(VerticalLayout wrapper) {
        FormLayout formLayout = new FormLayout();
        // TODO Breite ist zu groß
        FormItem pnItem = addFormItem(wrapper, formLayout, personalnummer, "Personalnummer");
        formLayout.setColspan(pnItem, 2);
        FormItem pinItem = addFormItem(wrapper, formLayout, pin, "PIN");
        formLayout.setColspan(pinItem, 2);
    }

    private void createButtonLayout(VerticalLayout wrapper) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        Button save = new Button("Registrieren");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> {
        	UserInfo user = new UserInfo(personalnummer.getValue(), pin.getValue());
        	if ( service.isRegistrationValid(user)) {
        		service.save(user);
        		VaadinHelper.showNotification("Registriert!", NotificationVariant.LUMO_SUCCESS);
        	} else {
        		VaadinHelper.showNotification("Fehler beim Registrieren!", NotificationVariant.LUMO_ERROR);
        	}
            
        });
        buttonLayout.add(save);
        wrapper.add(buttonLayout);
    }

    private FormLayout.FormItem addFormItem(VerticalLayout wrapper, FormLayout formLayout, Component field, String fieldName) {
        FormLayout.FormItem formItem = formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
        return formItem;
    }

}

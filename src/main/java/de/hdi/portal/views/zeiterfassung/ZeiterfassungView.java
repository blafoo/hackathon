package de.hdi.portal.views.zeiterfassung;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import de.hdi.portal.backend.PortalService;
import de.hdi.portal.backend.UserInfo;
import de.hdi.portal.views.main.MainView;

@Route(value = "zeit", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Zeiterfassung")
@CssImport("styles/views/hdi/hdi-view.css")
public class ZeiterfassungView extends Div {

	private static final long serialVersionUID = -3999057735999935719L;
	
	@Autowired
	private PortalService service;
	
	private UserInfo user;
	
    public ZeiterfassungView() {
    	
        setId("zeit-view");
        VerticalLayout layout = createWrapper();

        createTitle(layout);
        createButtonLayout(layout);

        add(layout);
    }
    
    @PostConstruct
    public void init() {
    	System.out.println("ZeiterfassungView.init");
    	UserInfo user = service.load();
    	if ( !service.isRegistrationValid(user) ) {
//    		this.getUI().ifPresent(ui -> ui.navigate(RegistrierungView.class));
    		System.out.println("Fuck");
    		UI.getCurrent().navigate(RegistrierungView.class);
    	}
    }

    private VerticalLayout createWrapper() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setId("wrapper");
        wrapper.setSpacing(false);
        return wrapper;
    }
    
    private void createTitle(VerticalLayout layout) {
        H1 h1 = new H1("Zeiterfassung");
        layout.add(h1);
    }

    private void createButtonLayout(VerticalLayout layout) {
    	
    	// TODO Buttons horizontal mittig ausrichten
    	// Layout/Icon aus Mitarbeiter Portal übernehmen
    	
    	Button kommen = new Button("Kommen");
        kommen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        kommen.addClickListener(e -> Notification.show(service.kommen(user)) );
        kommen.setWidth("150px");
        kommen.setHeight("100px");
        
        Button gehen = new Button("Gehen");
        gehen.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        gehen.addClickListener(e -> Notification.show(service.gehen(user)) );
        gehen.setWidth("150px");
        gehen.setHeight("100px");
        
        layout.add(kommen, gehen);
    }
    
}

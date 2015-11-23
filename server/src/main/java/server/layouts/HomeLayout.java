package server.layouts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Generates the content of the home screen.
 * 
 * @author Sydney
 *
 */
public class HomeLayout {

	public VerticalLayout createLayout() {

		// Base layout containing all the things
		VerticalLayout base = new VerticalLayout();
		base.setMargin(new MarginInfo(false, true, true, true));
		base.setSpacing(true);


		/*
		 * Yes these seem pretty pointless, but the only way to properly create
		 * spaces between things that are next to each other in Vaadin is by
		 * sticking a empty label between bits and setting expand ratios.
		 * Learn from this!!
		 */
		Label rightPad = new Label("");
		Label leftPad = new Label("");

		// Textnesses
		Label welcome = new Label("Crowd Tidbits management console");
		welcome.setStyleName("h1");
		HorizontalLayout welcomeBar = new HorizontalLayout();
		welcomeBar.addComponent(leftPad);
		welcomeBar.addComponent(welcome);
		welcomeBar.addComponent(rightPad);
		welcomeBar.setExpandRatio(leftPad, 1);
		welcomeBar.setExpandRatio(welcome, 1);
		welcomeBar.setExpandRatio(rightPad, 1);

		// Services button
		Button services = new Button("Users");
		HorizontalLayout servicesBar = new HorizontalLayout();
		servicesBar.addComponent(leftPad);
		servicesBar.addComponent(services);
		servicesBar.addComponent(rightPad);
		servicesBar.setExpandRatio(leftPad, 1);
		servicesBar.setExpandRatio(services, 1);
		servicesBar.setExpandRatio(rightPad, 1);

		// Settings button
		Button settings = new Button("Posts");
		HorizontalLayout settingsBar = new HorizontalLayout();
		settingsBar.addComponent(leftPad);
		settingsBar.addComponent(settings);
		settingsBar.addComponent(rightPad);
		settingsBar.setExpandRatio(leftPad, 1);
		settingsBar.setExpandRatio(settings, 1);
		settingsBar.setExpandRatio(rightPad, 1);

		// Button stylage
		services.setWidth("300px");
		settings.setWidth("300px");

		services.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7631857433453198284L;

			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getPage().setLocation("/crowdbits/ui/users");
			}
		});

		settings.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 500762191593964118L;

			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getPage().setLocation("/crowdbits/ui/posts");
			}
		});

		base.addComponent(welcomeBar);
		base.addComponent(servicesBar);
		base.addComponent(settingsBar);

		base.setComponentAlignment(welcomeBar, Alignment.MIDDLE_CENTER);
		base.setComponentAlignment(servicesBar, Alignment.MIDDLE_CENTER);
		base.setComponentAlignment(settingsBar, Alignment.MIDDLE_CENTER);

		return base;
	}

	@SuppressWarnings("unused")
	private Logger getLog() {
		return LoggerFactory.getLogger(getClass());
	}
}

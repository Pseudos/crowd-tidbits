package server.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.layouts.HomeLayout;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The container of the homepage. With all the links.
 * 
 * @author Sydney
 * 
 */
@Component
public class HomePage extends UI {
	private static final long serialVersionUID = 2103259014064311993L;

	@Autowired
	private HomeLayout homeLayout;

	protected void init(VaadinRequest request) {
		VerticalLayout view = homeLayout.createLayout();
		setContent(view);
	}
}

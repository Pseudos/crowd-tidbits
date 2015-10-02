package server.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.layouts.UserAdminLayout;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The container of the user management page
 * 
 * @author Sydney
 * 
 */
@Component
public class UsersUI extends UI {
	private static final long serialVersionUID = -7307736369752454256L;

	@Autowired
	private UserAdminLayout userAdminLayout;

	@Override
	protected void init(VaadinRequest request) {
		VerticalLayout view = userAdminLayout.createLayout();
		setContent(view);
	}
}

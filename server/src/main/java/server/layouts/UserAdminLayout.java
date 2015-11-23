package server.layouts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import server.dao.UserDao;
import server.entity.User;
import server.util.HMAC;

import com.jensjansson.pagedtable.ControlsLayout;
import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.Item;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.SystemError;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Generates the content for the user administration page.
 * 
 * @author Sydney
 * 
 */
@Component
public class UserAdminLayout {

//	@Autowired
//	UserRolesDao userRolesDao;
	@Autowired
	UserDao userDao;

	private TextField username = new TextField("Username:");
	private TextField email = new TextField("Email:");
	private PasswordField password = new PasswordField("New Password:");
	private PasswordField passwordRep = new PasswordField("Repeat Password:");
	private OptionGroup status = new OptionGroup("Status:");
//	private ComboBox roles = new ComboBox("Roles:");
	List<String> assignRoles = new ArrayList<String>();
	boolean modifying = false;
	boolean shouldModify = false;

	PagedTable table = new PagedTable();
	IndexedContainer container = new IndexedContainer();
	
	ControlsLayout controls;

	public VerticalLayout createLayout() {
		// List of security roles the user has
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		Collection<? extends GrantedAuthority> userRoles = securityContext.getAuthentication().getAuthorities();
//
//		List<String> roleList = new ArrayList<String>();
//		for (Object role : userRoles.toArray()) {
//			roleList.add(role.toString());
//		}

		// Base layout for all the things
		VerticalLayout base = new VerticalLayout();

		// Base layout of the table
		VerticalLayout tableBase = new VerticalLayout();
		tableBase.setMargin(new MarginInfo(true, true, false, true));
		tableBase.setSpacing(true);
		controls = table.createControls();
		table.setSelectable(false);

		// Search bar
		final TextField searchBar = new TextField("Search:");
		searchBar.setImmediate(true);

//		// Drop down
//		final ComboBox select = new ComboBox("Roles");
//		select.setTextInputAllowed(false);
//		select.setNullSelectionAllowed(false);
//
//		// Add gateways to the dropdown list
//		select.addItem("All");
//		for (String role : userRolesDao.findUnique()) {
//			select.addItem(role);
//		}
//		select.select("All");

		// Text change listener for the search bar
		searchBar.addTextChangeListener(new TextChangeListener() {
			private static final long serialVersionUID = 3717668752681575044L;

			public void textChange(TextChangeEvent event) {
				searchBar.setValue(event.getText());
				String searchTerm = searchBar.getValue();
				applyFilters(searchTerm);
			}
		});

		// Selection listener for drop down
//		select.addValueChangeListener(new ValueChangeListener() {
//			private static final long serialVersionUID = 1579602483572081995L;
//
//			public void valueChange(ValueChangeEvent event) {
//				String searchTerm = searchBar.getValue();
//				applyFilters(searchTerm, (String) select.getValue());
//			}
//		});

		// Logout button
		final Button logout = new Button("Logout");
		addLogoutListener(logout);

		final Button home = new Button("Home");
		addHomeListener(home);

		// Top bar, containing search and logout
		HorizontalLayout topBar = new HorizontalLayout();
		topBar.addComponent(searchBar);
//		topBar.addComponent(select);
		topBar.addComponent(home);
		topBar.addComponent(logout);
		topBar.setSizeFull();
		topBar.setWidth("100%");
		topBar.setExpandRatio(searchBar, 2);
//		topBar.setExpandRatio(select, 2);
		topBar.setExpandRatio(home, 12);
		topBar.setExpandRatio(logout, 1);
		topBar.setComponentAlignment(home, Alignment.MIDDLE_RIGHT);
		topBar.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT);

		tableBase.addComponent(topBar);
		tableBase.addComponent(table);
		tableBase.addComponent(controls);
		tableBase.setComponentAlignment(controls, Alignment.MIDDLE_CENTER);

		// Fill the container with stuff
		buildContainer();

		table.setSizeFull();
		controls.setSizeUndefined();
		table.setImmediate(true);
		table.setSortEnabled(true);
		table.setContainerDataSource(container);

		// FORM
		// Container of complete form
		VerticalLayout form = new VerticalLayout();
		form.setMargin(new MarginInfo(false, true, false, true));
		form.setSizeUndefined();
		form.setSpacing(true);

		// Add stuff to the layout
		form.addComponent(username);
		username.setRequired(true);
		setThings(username);
		form.addComponent(email);
		email.setRequired(true);
		setThings(email);
		form.addComponent(password);
		setThings(password);
		form.addComponent(passwordRep);
		setThings(passwordRep);

		status.addItem("Enabled");
		status.addItem("Disabled");
		status.setMultiSelect(false);
		status.select("Enabled");
		form.addComponent(status);

//		roles.setNullSelectionAllowed(false);
//		roles.setTextInputAllowed(false);
//		if (roleList.contains("ROLE_IT")) {
//			roles.addItem("IT");
//		}
//		roles.addItem("Admin");
//		roles.addItem("User");
//		roles.select("User");
//
//		form.addComponent(roles);

		Button submit = new Button("Submit");
		addSubmitListener(submit, tableBase);
		Button cancel = new Button("Cancel");
		addCancelListener(cancel);

		// Contains the submit and cancel buttons
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponent(submit);
		buttons.addComponent(cancel);
		buttons.setSpacing(true);
		form.addComponent(buttons);

		base.addComponent(tableBase);
		base.addComponent(form);
		return base;
	}

	private void addHomeListener(Button home) {
		home.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -1619007343333477254L;

			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getPage().setLocation("/crowdbits/ui/");
			}
		});
	}

	private void addLogoutListener(final Button logout) {
		logout.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5586468158119451001L;

			public void buttonClick(ClickEvent event) {
				try {
					UI.getCurrent().getSession().close();
					UI.getCurrent().getSession().getService().closeSession(VaadinSession.getCurrent());
					UI.getCurrent().close();
					UI.getCurrent().detach();
					UI.getCurrent().getPage().setLocation("/crowdbits/logout");
				}
				catch (Exception e) {
					logout.setComponentError(new SystemError("Some error occured on the server side"));
					getLog().debug("Logout button broke", e);
				}
			}
		});
	}

	/**
	 * Sets some things on the textfield
	 * 
	 * @param tf
	 *            textfield
	 */
	private void setThings(TextField tf) {
		tf.setImmediate(true);
		tf.setValidationVisible(true);
		tf.setNullRepresentation("");
	}

	/**
	 * Sets some things on the password fields
	 * 
	 * @param tf
	 *            password field
	 */
	private void setThings(PasswordField tf) {
		tf.setImmediate(true);
		tf.setValidationVisible(true);
		tf.setNullRepresentation("");
	}

	/**
	 * Builds the data container and fills it with the data from the datasource
	 * Manual build, since datasource container can't load data to text fields
	 */
	@SuppressWarnings("unchecked")
	private void buildContainer() {
		// Add columns to the container
		container.addContainerProperty("Username", String.class, null);
		container.addContainerProperty("Status", String.class, null);
		container.addContainerProperty("Email", String.class, "");
		container.addContainerProperty("Manage", Button.class, null);

		// Empty the container, to generate again (for reload)
		container.removeAllItems();

		// Stick the values in the table
		List<User> users = userDao.findAll();
		for (User user : users) {
			String username = user.getUsername();

				// Create new item
				container.addItem(username);
				Item newItem = container.getItem(username);
				newItem.getItemProperty("Username").setValue(username);

				String value = user.getEnabled() == false ? "Disabled" : "Enabled";
				newItem.getItemProperty("Status").setValue(value);

				newItem.getItemProperty("Email").setValue(user.getEmail());

				// Add the "edit" links
				Button link = new Button("Edit");
				link.setStyleName("link");
				newItem.getItemProperty("Manage").setValue(link);
				addEditListener(newItem);
			}
	}

	/**
	 * Adds click listener to the cancel button
	 * 
	 * @param cancel
	 *            The cancel button
	 */
	private void addCancelListener(final Button cancel) {
		cancel.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 5586468158119451001L;

			public void buttonClick(ClickEvent event) {
				try {
					if (modifying == true) {
						username.setEnabled(true);
						email.setEnabled(true);
						modifying = false;
					}
					clearForm();
				}
				catch (Exception e) {
					cancel.setComponentError(new SystemError("Some error occured on the server side"));
					getLog().debug("Logout button broke", e);
				}
			}
		});
	}

	/**
	 * Add click listener for the submit button
	 * 
	 * @param submit
	 *            The submit button
	 */
	private void addSubmitListener(final Button submit, final VerticalLayout tableBase) {
		submit.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -2823716966112734911L;
			String frmPassword = "";

			public void buttonClick(ClickEvent event) {
				try {
					// Validate form
					if (username.getValue() == null || username.getValue().isEmpty()) {
						username.setComponentError(new SystemError("Please fill in username"));
						return;
					}
					else {
						username.setComponentError(null);
					}
	                if (email.getValue() == null || email.getValue().isEmpty()) {
	                    email.setComponentError(new SystemError("Please fill in username"));
	                    return;
	                }
	                else {
	                    email.setComponentError(null);
	                }

					// Retrieve values from the form, for convenience
					String frmUsername = username.getValue();
					String frmEmail = email.getValue();
			        

					if (password.getValue() == null && passwordRep.getValue() == null) {
						password.setComponentError(null);
					}
					else if (password.getValue().equals(passwordRep.getValue())) {
						password.setComponentError(null);
						frmPassword = HMAC.getHmac(password.getValue(),frmUsername);
					}
					else {
						throw new PasswordException("The passwords do not match");
					}
					

					final boolean frmStatus = status.getValue().equals("Enabled") ? true : false;

					// Get existing things for that user
					final User existing = userDao.findByEmailAndUsername(frmEmail, frmUsername);
//					getLog().debug("Records found for username {}", existing.size());

					// User does not exist, add
					if (existing == null) {
						User newUser = new User();
						newUser.setEnabled(frmStatus);
						newUser.setUsername(frmUsername);
						newUser.setEmail(frmEmail);
						newUser.setPassword(frmPassword);

//						// UserRoles entry for each ticked role
//						for (String selected : assignRoles) {
//							UserRoles newRole = new UserRoles();
//							newRole.setRole(selected);
//							newRole.setUsername(newUser);
//							userRolesDao.create(newRole);
//						}

//						assignRoles.clear();
						userDao.create(newUser);
						clearForm();
						buildContainer();
						table.refreshRowCache();
					}
					/*
					 * IF the user DOES exist
					 */
					else {
						// If the user didn't click "Edit" ask for edit
						// confirmation
						if (modifying == false) {
							String confirmationMessage = "The user already exists. Continuing will modify this user.";
							final String cancelNotice = "To create a new user, please change the user name and/or email.";
							ConfirmDialog.show(UI.getCurrent(), confirmationMessage, new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;

								public void onClose(ConfirmDialog dialog) {
									// YES
									if (dialog.isConfirmed()) {
										modifyUser(existing, frmStatus, frmPassword);
									}
									else {
										Notification.show(cancelNotice, Notification.Type.WARNING_MESSAGE);
									}
								}
							});
						}
						else {
							modifyUser(existing, frmStatus, frmPassword);
						}
					}
				}
				catch (Exception e) {
					submit.setComponentError(new SystemError("Some error occured on the server side"));
					getLog().debug("Submit button broke", e);
				}
				catch (PasswordException e) {
					password.setComponentError(new UserError(e.getMessage()));
				}
				
				if(container.getItemIds().size()!=0 && (container.getItemIds().size()-1)%Integer.parseInt((String)controls.getItemsPerPageSelect().getValue())==0)
				{
					buildContainer();
					table.refreshRowCache();
					ControlsLayout newControls = table.createControls();
					newControls.setSizeUndefined();
					tableBase.replaceComponent(controls, newControls);
					tableBase.setComponentAlignment(newControls, Alignment.MIDDLE_CENTER);
					controls = newControls;
					table.nextPage();
					//UI.getCurrent().getPage().reload();
				}
			}
		});
	}

	/**
	 * Modifies an existing user to reflect form values
	 * 
	 * @param existing
	 *            List of existing userRoles
	 * @param frmStatus
	 *            Disables/Enabled user status
	 * @param frmPassword
	 *            Password
	 */
	private void modifyUser(User existing, boolean frmStatus, String frmPassword) {
		// Update user things, since either editing or said YES
		User user = existing;
		user.setEnabled(frmStatus);
		
		if(frmPassword!=null && !frmPassword.isEmpty())
		{
		    user.setPassword(HMAC.getHmac(frmPassword, username.getValue()));
		}
		
		//user.setPassword(frmPassword);
		userDao.update(user);

		// Update roles
//		getLog().debug("AssignRoles: {}", assignRoles);
//		List<String> existingRoles = new ArrayList<String>();
//		for (UserRoles role : existing) {
//			existingRoles.add(role.getRole());
//			getLog().debug("Existing roles: {}", existingRoles);
//
//			// Delete - selected does not contain existing
//			if (!assignRoles.contains(role.getRole())) {
//				getLog().debug("Deleting: {}", role);
//				userRolesDao.delete(role);
//			}
//		}
//		for (String selectedRole : assignRoles) {
//			// Create - existing does not contain selected
//			if (!existingRoles.contains(selectedRole)) {
//				UserRoles newRole = new UserRoles();
//				newRole.setRole(selectedRole);
//				newRole.setUsername(existing.get(0).getUsername());
//				userRolesDao.create(newRole);
//			}
//		}

		// Exit modification mode
		if (modifying == true) {
			username.setEnabled(true);
			email.setEnabled(true);
			modifying = false;
		}
		assignRoles.clear();
		clearForm();

		// Rebuild the container, to ensure that new stuff is there
		getLog().debug("Rebuilding container thing!");
		buildContainer();
		table.refreshRowCache();
	}

	/**
	 * Filters the container for search term and gateway selection
	 * 
	 * @param searchTerm
	 *            Search term from the search bar
	 * @param select
	 *            Value from the drop down selection
	 */
	private void applyFilters(String searchTerm) {
		// Remove existing filters
		container.removeAllContainerFilters();

		// Filter for all searchable fields
		Filter f = new SimpleStringFilter("Username", searchTerm, true, false);
		Filter g = new SimpleStringFilter("Email", searchTerm, true, false);

		// Filter and refresh table rows
		container.addContainerFilter(f);
		container.addContainerFilter(g);
		table.refreshRowCache();
		table.setCurrentPage(1);
	}

	/**
	 * Creates the edit button, and adds a click listener to it
	 * 
	 * @param item
	 *            Row in the table
	 */
	public void addEditListener(final Item item) {
		Button link = (Button) item.getItemProperty("Manage").getValue();
		link.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 7719431071218078273L;

			public void buttonClick(ClickEvent event) {
				clearForm();
				// Retrieve all records that have that ussd String
				User serv = userDao.findByEmailAndUsername((String) item.getItemProperty("Email").getValue(),(String) item.getItemProperty("Username").getValue());
				// Set form fields to these values
				username.setValue(serv.getUsername());
				email.setValue(serv.getEmail());

				if (serv.getEnabled() == false) {
					status.select("Disabled");
				}
				else {
					status.select("Enabled");
				}

				// Disable text fields for the USSD string
				username.setEnabled(false);
				email.setEnabled(false);
				modifying = true;
			}
		});
	}

	/**
	 * Clear all text fields and tick boxes
	 */
	public void clearForm() {
		username.setValue(null);
		password.setValue(null);
		passwordRep.setValue(null);

		username.setComponentError(null);
		password.setComponentError(null);
		email.setComponentError(null);

		status.unselect("Disabled");
		status.select("Enabled");

	}

	private Logger getLog() {
		return LoggerFactory.getLogger(getClass());
	}
}

class PasswordException extends Throwable {
	private static final long serialVersionUID = 3881120532536949599L;

	public PasswordException(String message) {
		super(message);
	}
}

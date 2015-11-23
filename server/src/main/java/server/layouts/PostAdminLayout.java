package server.layouts;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.dao.PostDao;
import server.dao.UserDao;
import server.entity.Post;
import server.entity.User;

import com.jensjansson.pagedtable.ControlsLayout;
import com.jensjansson.pagedtable.PagedTable;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.SystemError;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Component
public class PostAdminLayout {
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;

    private TextField description = new TextField("Description:");
    private TextField email = new TextField("Email:");
    private TextField latitude = new TextField("Latitude:");
    private TextField longitude = new TextField("Longitude:");
    private OptionGroup status = new OptionGroup("Priority:");

    PagedTable table = new PagedTable();
    IndexedContainer container = new IndexedContainer();

    ControlsLayout controls;

    public VerticalLayout createLayout() {
        if(postDao==null)
        {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! WTF");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        
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

        // Drop down
        final ComboBox select = new ComboBox("Priority");
        select.setTextInputAllowed(false);
        select.setNullSelectionAllowed(false);

        // Add values to the dropdown list
        select.addItem("All");
        select.addItem("1");
        select.addItem("2");
        select.addItem("3");
        select.addItem("4");
        select.select("All");

        // Text change listener for the search bar
        searchBar.addTextChangeListener(new TextChangeListener() {
            private static final long serialVersionUID = 3717668752681575044L;

            public void textChange(TextChangeEvent event) {
                searchBar.setValue(event.getText());
                String searchTerm = searchBar.getValue();
                applyFilters(searchTerm, (String) select.getValue());
            }
        });

        // Selection listener for drop down
        select.addValueChangeListener(new ValueChangeListener() {
            private static final long serialVersionUID = 1579602483572081995L;

            public void valueChange(ValueChangeEvent event) {
                String searchTerm = searchBar.getValue();
                applyFilters(searchTerm, (String) select.getValue());
            }
        });

        // Logout button
        final Button logout = new Button("Logout");
        addLogoutListener(logout);

        final Button home = new Button("Home");
        addHomeListener(home);

        // Top bar, containing search and logout
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.addComponent(searchBar);
        topBar.addComponent(select);
        topBar.addComponent(home);
        topBar.addComponent(logout);
        topBar.setSizeFull();
        topBar.setWidth("100%");
        topBar.setExpandRatio(searchBar, 2);
        topBar.setExpandRatio(select, 2);
        topBar.setExpandRatio(home, 10);
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
        form.addComponent(description);
        description.setRequired(true);
        setThings(description);
        form.addComponent(email);
        email.setRequired(true);
        setThings(email);
        form.addComponent(latitude);
        latitude.setRequired(true);
        setThings(latitude);
        form.addComponent(longitude);
        longitude.setRequired(true);
        setThings(longitude);

        status.addItem("0");
        status.addItem("1");
        status.addItem("2");
        status.setMultiSelect(false);
        status.select("2");
        form.addComponent(status);

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
                    UI.getCurrent().getSession().getService()
                            .closeSession(VaadinSession.getCurrent());
                    UI.getCurrent().close();
                    UI.getCurrent().detach();
                    UI.getCurrent().getPage().setLocation("/crowdbits/logout");
                } catch (Exception e) {
                    logout.setComponentError(new SystemError(
                            "Some error occured on the server side"));
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
     * Builds the data container and fills it with the data from the datasource
     * Manual build, since datasource container can't load data to text fields
     */
    @SuppressWarnings("unchecked")
    private void buildContainer() {
        // Add columns to the container
        container.addContainerProperty("ID", Long.class, 0L);
        container.addContainerProperty("Description", String.class, null);
        container.addContainerProperty("Email", String.class, null);
        container.addContainerProperty("Latitude", String.class, "");
        container.addContainerProperty("Longitude", String.class, "");
        container.addContainerProperty("Priority", String.class, "");
        container.addContainerProperty("Date", Date.class, null);
        container.addContainerProperty("Manage", Button.class, null);

        // Empty the container, to generate again (for reload)
        container.removeAllItems();

        // Stick the values in the table
        List<Post> posts = postDao.findAll();
        for (Post post : posts) {
            // String username = user.getUsername();

            // Create new item
            container.addItem(post.getId());
            Item newItem = container.getItem(post.getId());

            newItem.getItemProperty("ID").setValue(post.getId());
            newItem.getItemProperty("Description").setValue(
                    post.getDescription());
            newItem.getItemProperty("Email").setValue(
                    post.getPoster().getEmail());
            newItem.getItemProperty("Latitude").setValue(Double.toString(post.getLatitude()));
            newItem.getItemProperty("Longitude").setValue(Double.toString(post.getLongitude()));
            newItem.getItemProperty("Priority").setValue(Integer.toString(post.getPriority()));
            newItem.getItemProperty("Date").setValue(post.getPostTime());

            // Add the "edit" links
            Button link = new Button("Delete");
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
                    clearForm();
                } catch (Exception e) {
                    cancel.setComponentError(new SystemError(
                            "Some error occured on the server side"));
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
    private void addSubmitListener(final Button submit,
            final VerticalLayout tableBase) {
        submit.addClickListener(new ClickListener() {
            private static final long serialVersionUID = -2823716966112734911L;

            public void buttonClick(ClickEvent event) {
                try {
                    // Validate form
                    if (description.getValue() == null || description.getValue().isEmpty()) {
                        description.setComponentError(new SystemError("Please fill in post content"));
                        return;
                    } else {
                        description.setComponentError(null);
                    }
                    if (email.getValue() == null || email.getValue().isEmpty()) {
                        email.setComponentError(new SystemError("Please fill in user email"));
                        return;
                    } else {
                        email.setComponentError(null);
                    }
                    if (latitude.getValue() == null || latitude.getValue().isEmpty()) {
                        latitude.setComponentError(new SystemError("Please fill in user latitude"));
                        return;
                    } else {
                        latitude.setComponentError(null);
                    }
                    if (longitude.getValue() == null || longitude.getValue().isEmpty()) {
                        longitude.setComponentError(new SystemError("Please fill in user longitude"));
                        return;
                    } else {
                        longitude.setComponentError(null);
                    }

                    // Retrieve values from the form, for convenience
                    String frmDescription = description.getValue();
                    String frmEmail = email.getValue();
                    String frmLat = latitude.getValue();
                    String frmLon = latitude.getValue();

                    String frmStatus = (String) status.getValue();

                    // Get existing things for that user
                    final User user = userDao.findByEmail(frmEmail);
                    if (user == null) {
                        email.setComponentError(new SystemError(
                                "User does not exist"));
                        return;
                    }

                    // getLog().debug("Records found for username {}",
                    // existing.size());

                    Post newPost = new Post();
                    newPost.setPriority(Integer.parseInt(frmStatus));
                    newPost.setDescription(frmDescription);
                    newPost.setPoster(user);
                    newPost.setLatitude(Double.parseDouble(frmLat));
                    newPost.setLongitude(Double.parseDouble(frmLon));
                    newPost.setPostTime(new Date());

                    postDao.create(newPost);
                    clearForm();
                    buildContainer();
                    table.refreshRowCache();

                } catch (Exception e) {
                    submit.setComponentError(new SystemError(
                            "Some error occured on the server side"));
                    getLog().debug("Submit button broke", e);
                }

                if (container.getItemIds().size() != 0
                        && (container.getItemIds().size() - 1)
                                % Integer.parseInt((String) controls
                                        .getItemsPerPageSelect().getValue()) == 0) {
                    buildContainer();
                    table.refreshRowCache();
                    ControlsLayout newControls = table.createControls();
                    newControls.setSizeUndefined();
                    tableBase.replaceComponent(controls, newControls);
                    tableBase.setComponentAlignment(newControls,
                            Alignment.MIDDLE_CENTER);
                    controls = newControls;
                    table.nextPage();
                    // UI.getCurrent().getPage().reload();
                }
            }
        });
    }

    /**
     * Filters the container for search term and gateway selection
     * 
     * @param searchTerm
     *            Search term from the search bar
     * @param priority
     * @param select
     *            Value from the drop down selection
     */
    private void applyFilters(String searchTerm, String priority) {
        // Remove existing filters
        container.removeAllContainerFilters();

        // Filter for all searchable fields
        Filter f = new SimpleStringFilter("Description", searchTerm, true, false);
        Filter g = new SimpleStringFilter("Email", searchTerm, true, false);
        
        if(!priority.equals("All"))
        {
            Filter p = new SimpleStringFilter("Priority", priority, true, false);
            container.addContainerFilter(p);
        }

        // Filter and refresh table rows
        container.addContainerFilter(f);
        container.addContainerFilter(g);
        
        table.refreshRowCache();
        table.setCurrentPage(1);
    }

    /**
     * Creates the delete button, and adds a click listener to it
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
                Post post = postDao.findPostById((Long) item.getItemProperty(
                        "ID").getValue());
                postDao.delete(post);

                buildContainer();
            }
        });
    }

    /**
     * Clear all text fields and tick boxes
     */
    public void clearForm() {
        description.setValue(null);
        latitude.setValue(null);
        longitude.setValue(null);
        email.setValue(null);

        description.setComponentError(null);
        latitude.setComponentError(null);
        longitude.setComponentError(null);
        email.setComponentError(null);

        status.unselect("0");
        status.unselect("1");
        status.select("2");

    }

    private Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }
}

package server.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.layouts.HomeLayout;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author Sydney
 *
 */

@Component
public class HomePage extends UI {
  @Autowired
  HomeLayout homeLayout;

  protected void init(VaadinRequest request) {
      VerticalLayout view = homeLayout.createLayout();
      setContent(view);
  }
}
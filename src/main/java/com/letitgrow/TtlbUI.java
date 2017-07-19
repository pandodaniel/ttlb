package com.letitgrow;

import com.google.common.eventbus.Subscribe;
import com.letitgrow.data.DataProvider;
import com.letitgrow.data.dummy.DummyDataProvider;
import com.letitgrow.domain.User;
import com.letitgrow.event.DashboardEvent;
import com.letitgrow.event.DashboardEventBus;
import com.letitgrow.view.LoginView;
import com.letitgrow.view.MainView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.*;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import javax.servlet.annotation.WebServlet;
import java.util.Locale;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@Widgetset("com.letitgrow.DashboardWidgetSet")
public class TtlbUI extends UI {

    /*
 * This field stores an access to the dummy backend layer. In real
 * applications you most likely gain access to your beans trough lookup or
 * injection; and not in the UI but somewhere closer to where they're
 * actually accessed.
 */
    private final DataProvider dataProvider = new DummyDataProvider();
    private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

    @Override
    protected void init(final VaadinRequest request) {
        setLocale(Locale.US);

        DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        updateContent();

        // Some views need to be aware of browser resize events so a
        // BrowserResizeEvent gets fired to the event bus on every occasion.
        Page.getCurrent().addBrowserWindowResizeListener(
                new Page.BrowserWindowResizeListener() {
                    @Override
                    public void browserWindowResized(
                            final Page.BrowserWindowResizeEvent event) {
                        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
                    }
                });
    }


    /**
     * Updates the correct content for this UI based on the current user status.
     * If the user is logged in with appropriate privileges, main view is shown.
     * Otherwise login view is shown.
     */
    private void updateContent() {
        User user = (User) VaadinSession.getCurrent()
                .getAttribute(User.class.getName());
        if (user != null && "admin".equals(user.getRole())) {
            // Authenticated user
            setContent(new MainView());
            removeStyleName("loginview");
        } else {
            setContent(new LoginView());
            addStyleName("loginview");
        }
    }

    @Subscribe
    public void userLoginRequested(final DashboardEvent.UserLoginRequestedEvent event) {
        User user = getDataProvider().authenticate(event.getUserName(),
                event.getPassword());
        if(user != null) {
            ((LoginView)getContent()).removeErrorMessage();
            VaadinSession.getCurrent().setAttribute(User.class.getName(), user);
            updateContent();
        }
        else {
            ((LoginView)getContent()).addErrorMessage();
        }
    }

    @Subscribe
    public void userLoggedOut(final DashboardEvent.UserLoggedOutEvent event) {
        // When the user logs out, current VaadinSession gets closed and the
        // page gets reloaded on the login screen. Do notice the this doesn't
        // invalidate the current HttpSession.
        VaadinSession.getCurrent().close();
        Page.getCurrent().reload();
    }

    @Subscribe
    public void closeOpenWindows(final DashboardEvent.CloseOpenWindowsEvent event) {
        for (Window window : getWindows()) {
            window.close();
        }
    }

    /**
     * @return An instance for accessing the (dummy) services layer.
     */
    public static DataProvider getDataProvider() {
        return ((TtlbUI) getCurrent()).dataProvider;
    }

    public static DashboardEventBus getDashboardEventbus() {
        return ((TtlbUI) getCurrent()).dashboardEventbus;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = TtlbUI.class, productionMode = false, widgetset="com.letitgrow.DashboardWidgetSet")
    public static class MyUIServlet extends VaadinServlet {
    }
}

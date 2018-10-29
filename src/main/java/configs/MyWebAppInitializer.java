package configs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import services.ScriptActions;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class MyWebAppInitializer implements WebApplicationInitializer {
    @Autowired
    private static Logger initLogger = LogManager.getLogger(MyWebAppInitializer.class.getName());

    public void onStartup(ServletContext servletContext) throws ServletException{
        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();
        context.register(configs.SpringConfig.class);
        context.setServletContext(servletContext);

        ServletRegistration.Dynamic dispatcher
                = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        try {
            Class.forName(ScriptActions.class.getName());
        }
        catch (ClassNotFoundException ex) {
            initLogger.error( "Initializing servlet failed", ex);
            throw new ServletException("Error by initializing servlet logic");
        }


    }
}

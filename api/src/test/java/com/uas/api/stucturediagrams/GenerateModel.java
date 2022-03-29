package com.uas.api.stucturediagrams;

import com.structurizr.Workspace;
import com.structurizr.analysis.*;
import com.structurizr.api.StructurizrClient;
import com.structurizr.model.*;
import com.structurizr.view.*;
import org.junit.jupiter.api.Test;

import java.io.File;

public class GenerateModel {
    private final static int WORKSPACE_ID=72910;
    private final static String API_KEY = "032a051b-ffd1-4d65-8eda-18a071d63b96";
    private final static String API_SECRET = "0d8991f5-7486-45e5-92b8-75ad0ff5d984";
//    @Test
    public void generateModel() throws Exception {


        Workspace workspace = new Workspace("UAS Lifecycle Management", "UAS Lifecycle Management is software collection consisting of UAS Lifecycle Management Android, UAS Lifecycle Management Web and UAS Lifecycle Management API");
        Model model = workspace.getModel();

        // create the basic model (the stuff we can't get from the code)
        // we have a system and we have (one) user.  There could be many more users/roles.
        SoftwareSystem uasLifeCycleAPI = model.addSoftwareSystem("UAS Lifecycle Management API", "UAS Lifecycle Management API is the Spring Boot and MySQL backend project providing JSON responses to both the Android and Web products.");
        Person customer = model.addPerson("UAS Lifecycle Management User", "An employee who uses the web and android app");
        SoftwareSystem uasLifecycleApp = model.addSoftwareSystem("UAS Lifecycle Management Mobile App", "An external mobile application which requires the use of the API");
        SoftwareSystem uasLifecycleWeb = model.addSoftwareSystem("UAS Lifecycle Management Web App", "An external web application which requires the use of the API");

        //set a relationship between the user(s) and the system.
        customer.uses(uasLifecycleApp, "Uses");
        customer.uses(uasLifecycleWeb, "Uses");
        uasLifecycleApp.uses(uasLifeCycleAPI, "Uses");
        uasLifecycleWeb.uses(uasLifeCycleAPI, "Uses");

        //create a SystemContext view for the system
        ViewSet viewSet = workspace.getViews();
        SystemContextView contextView = viewSet.createSystemContextView(uasLifeCycleAPI, "context", "The System Context diagram for the UAS Lifecycle Management System.");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        uasLifeCycleAPI.addTags("UAS Lifecycle Management API");

        Styles styles = viewSet.getConfiguration().getStyles();
        styles.addElementStyle("UAS Lifecycle Management API").background("#6CB33E").color("#ffffff");
        styles.addElementStyle(Tags.PERSON).background("#519823").color("#ffffff").shape(Shape.Person);


        // Now move down a layer to the container view.

        Container webApplication = uasLifeCycleAPI.addContainer(
                "Spring Boot Application", "The web application", "Embedded web container.  Tomcat 9.0.53");
        Container relationalDatabase = uasLifeCycleAPI.addContainer(
                "Relational Database", "Stores information about athletes, their sessions and competitions", "MySQL/Maria DB");
        uasLifecycleApp.uses(webApplication, "Uses", "JSON/HTTPS");
        uasLifecycleWeb.uses(webApplication, "Uses", "JSON/HTTPS");
        webApplication.uses(relationalDatabase, "Reads from and writes to", "JDBC (via JPA), port 3306");

        ContainerView containerView = viewSet.createContainerView(uasLifeCycleAPI, "containers", "The Containers diagram for the UAS Lifecycle Management System.");
        containerView.addAllPeople();
        containerView.addAllSoftwareSystems();
        containerView.addAllContainers();

        styles.addElementStyle(Tags.CONTAINER).background("#91D366").color("#ffffff");
        styles.addElementStyle("Database").shape(Shape.Cylinder);

        String path = System.getProperty("user.dir");

        System.out.println("Working Directory = " + path);

        String path2 = path + "\\com\\uas\\api\\src\\main\\java\\";

        File sourceRoot = new File(path);
        File mainFolder = new File(path2);

        // and now automatically find all Spring @Controller, @RestController, @Component, @Service and @Repository components
        ComponentFinder componentFinder = new ComponentFinder(webApplication, "com.uas.api",
                new SpringComponentFinderStrategy(
                        new ReferencedTypesSupportingTypesStrategy()
                ),
                new SourceCodeComponentFinderStrategy(new File(path2), 150)

        );

        componentFinder.findComponents();

        ComponentFinder securityConfigFinder = new ComponentFinder(
                webApplication,
                "com.uas.api",
                new TypeMatcherComponentFinderStrategy(
                        new NameSuffixTypeMatcher("SecurityConfig", "The configuration for security in the application", "Java - Spring Security")
                )
        );
        securityConfigFinder.findComponents();

        // connect the customer to all of the Spring MVC controllers
//        webApplication.getComponents().stream()
//                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_MVC_CONTROLLER))
//                .forEach(c -> customer.uses(c, "Uses", "HTTP"));

        // connect the external service to all of the Spring Rest controllers
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REST_CONTROLLER))
                .forEach(c -> uasLifecycleApp.uses(c, "Uses", "HTTP"));
        // connect the external service to all of the Spring Rest controllers
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REST_CONTROLLER))
                .forEach(c -> uasLifecycleWeb.uses(c, "Uses", "HTTP"));

        // connect all of the repository components to the relational database
        webApplication.getComponents().stream()
                .filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REPOSITORY))
                .forEach(c -> c.uses(relationalDatabase, "Reads from and writes to", "JDBC"));

        //Let's see what is being scanned


        for (Component c : webApplication.getComponents()) {
            System.out.println(c.getCanonicalName() + " : " + c.getTechnology());
        }

        ComponentView componentView = viewSet.createComponentView(webApplication, "components", "The Components diagram for the Athletics Hub mobile application and API system.");
        componentView.addAllComponents();
        componentView.addAllPeople();
        componentView.add(uasLifecycleApp);
        componentView.add(uasLifecycleWeb);
        componentView.add(relationalDatabase);

        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_MVC_CONTROLLER)).forEach(c -> c.addTags("Spring MVC Controller"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REST_CONTROLLER)).forEach(c -> c.addTags("Spring REST Controller"));

        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_SERVICE)).forEach(c -> c.addTags("Spring Service"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().equals(SpringComponentFinderStrategy.SPRING_REPOSITORY)).forEach(c -> c.addTags("Spring Repository"));
        webApplication.getComponents().stream().filter(c -> c.getTechnology().contains("Config")).forEach(c -> c.addTags("Configurations"));
        relationalDatabase.addTags("Database");


        styles.addElementStyle("Spring REST Controller").background("#D4FFC6").color("#000000");

        styles.addElementStyle("Spring Service").background("#6CB33E").color("#000000");
        styles.addElementStyle("Spring Repository").background("#95D46C").color("#000000");
        styles.addElementStyle("Components").background("#eeeeee").color("#000077").shape(Shape.RoundedBox);
        styles.addElementStyle("Configurations").background("#9ced68").color("#000000");


        StructurizrClient structurizrClient = new StructurizrClient(API_KEY, API_SECRET);
        structurizrClient.putWorkspace(WORKSPACE_ID, workspace);
    }



}


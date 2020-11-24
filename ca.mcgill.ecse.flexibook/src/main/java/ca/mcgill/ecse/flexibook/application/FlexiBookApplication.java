/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca.mcgill.ecse.flexibook.application;

import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.model.User;
import ca.mcgill.ecse.flexibook.persistence.FlexiBookPersistence;
import ca.mcgill.ecse.flexibook.view.FlexiBookPage;
import ca.mcgill.ecse.flexibook.view.LandingPage;


public class FlexiBookApplication {
	public static final boolean LOAD_PERSISTENCE = true;
	private static FlexiBook flexiBook;
    private static User currentUser;
	
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        // System.out.println(new FlexiBookApplication().getGreeting());
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LandingPage().setVisible(true);
            }
        });
    }
    
    public static FlexiBook getFlexiBook() {
    	if (flexiBook == null) {
    		if (LOAD_PERSISTENCE) {
    			flexiBook = FlexiBookPersistence.load();
    		} else {
    			flexiBook = new FlexiBook();
    		}
    	}
    	
    	return flexiBook;
    }
    
    public static boolean hasCurrentUser() {
    	return currentUser != null;
    }
    
    public static User getCurrentUser() {
    	return currentUser;
    }
    
    public static void setCurrentUser(User user) {
    	if (user == null) {
    		throw new IllegalArgumentException("Current user cannot be set to null"); // use #unsetCurrentUser
    	}
    	currentUser = user;
    }
    
    public static void unsetCurrentUser() {
    	currentUser = null;
    }
}

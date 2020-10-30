/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ca.mcgill.ecse.flexibook.application;

import org.junit.jupiter.api.Test;

import ca.mcgill.ecse.flexibook.model.Business;
import ca.mcgill.ecse.flexibook.model.FlexiBook;
import ca.mcgill.ecse.flexibook.persistence.FlexiBookPersistence;
import ca.mcgill.ecse.flexibook.util.SystemTime;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

class FlexiBookApplicationTest {
    @Test void appHasAGreeting() {
        FlexiBookApplication classUnderTest = new FlexiBookApplication();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }


    private static int nextDriverID = 1;
	private static String filename = "testdata.btms";
	
	@BeforeAll
	public static void setUpOnce() {
		FlexiBookPersistence.setFilename(filename);
	}
	
	@BeforeEach
	public void setUp() {
		// remove test file
		File f = new File(filename);
		f.delete();
		// clear all data
		FlexiBook flexibook = FlexiBookApplication.getFlexiBook();
		flexibook.delete();
	}

	@Test
	public void testPersistence() {
		FlexiBook flexiBook = FlexiBookApplication.getFlexiBook();
        Business b = new Business("BusinessTest", "101McGill", "5148888888", "leeroy@jenkins.com", flexiBook);
        flexiBook.setBusiness(b);

		FlexiBookPersistence.save(flexiBook);
		
		// load model again and check it
		FlexiBook flexiBook2 = FlexiBookPersistence.load();
        assertEquals(b.getName(), flexiBook2.getBusiness().getName());
        assertEquals(b.getAddress(), flexiBook2.getBusiness().getAddress());;
        assertEquals(b.getPhoneNumber(), flexiBook2.getBusiness().getPhoneNumber());;
        assertEquals(b.getEmail(), flexiBook2.getBusiness().getEmail());;
	}
}

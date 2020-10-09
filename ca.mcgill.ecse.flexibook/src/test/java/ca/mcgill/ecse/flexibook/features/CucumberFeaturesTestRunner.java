package ca.mcgill.ecse.flexibook.features;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/resources", glue = "ca.mcgill.ecse.flexibook.features")
public class CucumberFeaturesTestRunner {
}


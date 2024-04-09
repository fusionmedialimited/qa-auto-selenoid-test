
package testRuns.abstracts;

import infrastructure.listeners.retry.common.CucumberAnnotationTransformer;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.Listeners;

@Listeners({CucumberAnnotationTransformer.class})
public abstract class AbstractTestNGCucumberRetriesTests extends AbstractTestNGCucumberTests {

}
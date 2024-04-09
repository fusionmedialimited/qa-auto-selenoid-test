package infrastructure.listeners;

import infrastructure.logger.Log;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventHandler;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;

import static infrastructure.ReportAttachments.cucumberReportCheckSize;

@SuppressWarnings("unused")
public class testEventListener implements ConcurrentEventListener {

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, setup);
        eventPublisher.registerHandlerFor(TestRunFinished.class, teardown);
    }

    private final EventHandler<TestRunStarted> setup = event -> beforeAll();

    private void beforeAll() {
        Log.info("Before all");
    }

    private final EventHandler<TestRunFinished> teardown = event -> afterAll();

    private void afterAll() {
        Log.info("After all");
        cucumberReportCheckSize(100);
    }
}

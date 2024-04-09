package infrastructure;

import hooks.Hooks;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.picocontainer.PicoFactory;
import pageObjects.pages.equities.BaseInstrumentPage;


public class PicoDependencyInjector implements ObjectFactory {

    private final PicoFactory delegate = new PicoFactory();

    public PicoDependencyInjector() {
        //driver
        addClass(Investing.class);

        //hooks
        addClass(Hooks.class);

        //Equities
        addClass(BaseInstrumentPage.class);
    }

    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void stop() {
        delegate.stop();
    }

    @Override
    public boolean addClass(Class<?> aClass) {
        return delegate.addClass(aClass);
    }

    @Override
    public <T> T getInstance(Class<T> aClass) {
        return delegate.getInstance(aClass);
    }
}

package pageObjects.factories;

import infrastructure.Investing;
import pageObjects.interfaces.equities.BaseInstrumentMethods;
import pageObjects.pages.equities.BaseInstrumentPage;

public class BaseInstrumentFactory {

    public BaseInstrumentMethods getPageObject(Investing investing) {
        if (investing.delegate == null) {
            return null;
        }
        return new BaseInstrumentPage((Investing) investing.delegate);
    }
}

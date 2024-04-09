package infrastructure.enums;

public enum Module {
    API("API"),
    DATA_WEB("Data-Web"),
    INFRASTRUCTURE("Infrastructure"),
    MOBILE("Mobile"),
    WEB("Web");

    private final String module;

    Module(String module) {
        this.module = module;
    }

    public String getModule() {
        return this.module;
    }
}

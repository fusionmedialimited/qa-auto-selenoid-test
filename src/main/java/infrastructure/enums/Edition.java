package infrastructure.enums;

public enum Edition {
    WWW, DE, ES, FR, IT, NL, PL, BR, PT, TR, RU,
    KR, JP, CN, HK, SA, SE, GR, ID, TH, VN, FI,
    IL, AU, CA, IN, NG, PH, ZA, HI, UK, MS, MX;

    public String toStringUpperCased() {
        return this.toString();
    }

    public String toStringLowerCased() {
        return this.toString().toLowerCase();
    }
}

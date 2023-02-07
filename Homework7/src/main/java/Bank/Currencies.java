package Bank;

public enum Currencies {
    USD("USD"),
    EUR("EUR"),
    UAH("UAH");

    private final String curr;

    Currencies(String curr) {
        this.curr = curr;
    }

    public String getCurr() {
        return curr;
    }
}

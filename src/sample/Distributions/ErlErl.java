package sample.Distributions;

public enum ErlErl {
    ALPHA(1.0),
    BETTA(1.2),
    m(2.0),
    k(2.0),
    M(1.0 / 1.2),
    D2(1.39);

    double value;
    ErlErl(double d){
        this.value = d;
    }

    public double getValue() {
        return value;
    }
}

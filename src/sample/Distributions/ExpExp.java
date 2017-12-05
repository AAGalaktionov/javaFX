package sample.Distributions;

public enum ExpExp {
    alpha(1.0),
    betta(1.0),
    M(1.0),
    D2(2.0);

    double value;
    ExpExp(double d){
        this.value = d;
    }

    public double getValue() {
        return value;
    }
}

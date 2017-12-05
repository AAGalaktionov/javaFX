package sample.Distributions;

public enum ParPar {

    at(4.0),
    bt(0.4),
    ay(4.0),
    by(0.4),
    M(1.0),
    D2(3.333),
    f(0.125),
    s(0.25);

    double value;
    ParPar(double d){
        this.value = d;
    }

    public double getValue() {
        return value;
    }
}

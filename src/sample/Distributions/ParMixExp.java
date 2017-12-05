package sample.Distributions;

public enum ParMixExp {
    ay(4.0),
    by(0.35),
    lambda1(1.0),
    lambda2(2.0),
    p(2.0/3.0),
    M(1.0/1.143),
    D2(2.304),
    q(1-p.getValue()),
    f(1.04),
    s(0.076);
    double value;
    ParMixExp(double d){
        this.value = d;
    }

    public double getValue() {
        return value;
    }
}

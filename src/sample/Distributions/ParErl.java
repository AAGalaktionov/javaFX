package sample.Distributions;

public enum ParErl {
    ay(4.0),
    by(0.4),
    betta(6.0),
    k(4.0),
    M(1.0/1.25),
    D2(1.2),
    f(2.73),
    s(-0.26);


    double value;
    ParErl(double d){
        this.value = d;
    }

    public double getValue() {
        return value;
    }
}

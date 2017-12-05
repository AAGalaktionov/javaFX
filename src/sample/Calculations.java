package sample;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.math3.special.Erf;
import sample.Distributions.*;

public class Calculations {
    public static Map<Double, Double> teorExp() {
        TreeMap<Double, Double> result = new TreeMap<>();

        double u = 10.0;


        for (double c = 0.05; c <= 2.0; c += 0.05) {
            //double res = Math.sqrt(c)*(u/c)*Math.exp(-u)
        }
        return null;
    }

    public static double normal(double x) {

        return 0.5 * (1.0 + Erf.erf(x / (Math.sqrt(2))));
    }

    public static BigDecimal creatBig(double d) {
        if (d == Double.POSITIVE_INFINITY || d == Double.NEGATIVE_INFINITY)
            return new BigDecimal(Double.MAX_VALUE);
        return new BigDecimal(String.valueOf(d));
    }

    public static BigDecimal sqrt(BigDecimal x) {
        return BigDecimal.valueOf(StrictMath.sqrt(x.doubleValue()));
    }

    public static BigDecimal div(BigDecimal x, BigDecimal y) {
        //return x.divide(y, 5, BigDecimal.ROUND_HALF_EVEN);

        return creatBig(x.doubleValue() / y.doubleValue());
    }


    public static TreeMap<Double, Double> calcM(double u, double t, BigDecimal c, BigDecimal incC, BigDecimal cEnd, double v, double M, double D2) {

        TreeMap<Double, Double> outMt = new TreeMap<>();
        while (cEnd.compareTo(c) >= 0) {
            double valC = c.doubleValue();
            double x1 = 1;
            double x2 = ((valC * (t - v)) / (u + valC * v)) + 1;
            double tmp1 = normal(((Math.sqrt(u + valC * v)) / (valC * Math.sqrt(D2) * Math.sqrt(x2))) * (x2 * (1 - valC * M) - 1));
            double tmp3 = normal(((Math.sqrt(u + valC * v)) / (valC * Math.sqrt(D2) * Math.sqrt(x1))) * (x1 * (1 - valC * M) - 1));
            double tmp2 = Math.exp(((2 * (u + valC * v)) / (valC * valC * D2)) * (1 - valC * M)) * normal(-(Math.sqrt(u + valC * v)) / (valC * Math.sqrt(D2 * x2)) * (x2 * (1 - valC * M) + 1));
            double tmp4 = Math.exp(((2 * (u + valC * v)) / (valC * valC * D2)) * (1 - valC * M)) * normal(-(Math.sqrt(u + valC * v)) / (valC * Math.sqrt(D2 * x1)) * (x1 * (1 - valC * M) + 1));
            double tmp = (tmp1 + tmp2) - (tmp3 + tmp4);
            outMt.put(valC, tmp);
            c = c.add(incC);
        }
        return outMt;
    }

    public static TreeMap<Double, Double> calcE(double u, double t, BigDecimal c, BigDecimal incC, BigDecimal cEnd, double v, double M, double D2, Type type) {

        TreeMap<Double, Double> outEt = new TreeMap<>();
        while (cEnd.compareTo(c) >= 0) {
            double valC = c.doubleValue();
            double x1 = 1;
            double x2 = ((valC * (t - v)) / (u + valC * v)) + 1;

            double tmp1 = normal(((Math.sqrt(u + valC * v)) / (valC * Math.sqrt(D2) * Math.sqrt(x2))) * (x2 * (1 - valC * M) - 1));
            double tmp3 = normal(((Math.sqrt(u + valC * v)) / (valC * Math.sqrt(D2) * Math.sqrt(x1))) * (x1 * (1 - valC * M) - 1));
            double tmp2 = Math.exp(((2 * (u + valC * v)) / (valC * valC * D2)) * (1 - valC * M)) * normal(-(Math.sqrt(u + valC * v)) / (valC * Math.sqrt(D2 * x2)) * (x2 * (1 - valC * M) + 1));
            double tmp4 = Math.exp(((2 * (u + valC * v)) / (valC * valC * D2)) * (1 - valC * M)) * normal(-(Math.sqrt(u + valC * v)) / (valC * Math.sqrt(D2 * x1)) * (x1 * (1 - valC * M) + 1));
            double tmp = (tmp1 + tmp2) - (tmp3 + tmp4);
            double Cf = 0, Cs = 0, Et = 0, Et3 = 0, Dt = 0, Ey = 0, Ey3 = 0, Dy = 0;

            switch (type) {
                case Exp_Exp:
                    Cf = ExpExp.betta.getValue() / (4 * ExpExp.alpha.getValue() * valC);
                    Cs = Cf;
                    break;
                case Par_MixExp:
                    Et3 = (6 * ParMixExp.p.getValue() * Math.pow(ParMixExp.q.getValue(), 2.0)) / (Math.pow(ParMixExp.lambda1.getValue(), 2.0) * ParMixExp.lambda2.getValue()) -
                            (6 * ParMixExp.q.getValue() * Math.pow(ParMixExp.p.getValue(), 2.0)) / (Math.pow(ParMixExp.lambda2.getValue(), 2.0) * ParMixExp.lambda1.getValue()) +
                            ((2 * ParMixExp.p.getValue() * (3 * ParMixExp.q.getValue() + Math.pow(ParMixExp.p.getValue(), 2.0))) / Math.pow(ParMixExp.lambda1.getValue(), 3.0)) +
                            ((2 * ParMixExp.q.getValue() * (3 * ParMixExp.p.getValue() + Math.pow(ParMixExp.q.getValue(), 2.0))) / Math.pow(ParMixExp.lambda2.getValue(), 3.0));
                    Et = ParMixExp.p.getValue() / ParMixExp.lambda1.getValue() + ParMixExp.q.getValue() / ParMixExp.lambda2.getValue();

                    Dt = (ParMixExp.q.getValue() * Math.pow(ParMixExp.lambda1.getValue(), 2.0) +
                            ParMixExp.p.getValue() * Math.pow(ParMixExp.lambda2.getValue(), 2.0) +
                            ParMixExp.p.getValue() * ParMixExp.q.getValue() * Math.pow(ParMixExp.lambda1.getValue() - ParMixExp.lambda2.getValue(), 2.0))
                            / (Math.pow(ParMixExp.lambda1.getValue(), 2.0) * Math.pow(ParMixExp.lambda2.getValue(), 2.0));
                    Ey3 = (2 * ParMixExp.ay.getValue() * (ParMixExp.ay.getValue() + 1)) / (Math.pow(ParMixExp.ay.getValue() - 1, 3.0) * (ParMixExp.ay.getValue() - 2) * (ParMixExp.ay.getValue() - 3) * Math.pow(ParMixExp.by.getValue(), 3.0));
                    Ey = 1 / (ParMixExp.by.getValue() * (ParMixExp.ay.getValue() - 1));
                    Dy = ParMixExp.ay.getValue() / (Math.pow(ParMixExp.ay.getValue() - 1, 2.0) * (ParMixExp.ay.getValue() - 2) * Math.pow(ParMixExp.by.getValue(), 2.0));
                    Cf = (Et3 / 2 * valC * D2 * Dt) * (((Et * Et) * Dy) / (D2 * Ey * Ey * Ey) - 1) - (((Et * Ey3) / (2 * valC * D2 * Ey * Dy)) * (Dt / (D2 * Ey) - 1)) + Et / (2 * valC * D2);
                    Cs = Et3 / (6 * valC * D2 * D2 * Ey) - (Et * Et * Et * Ey3) / (6 * valC * D2 * D2 * Ey * Ey * Ey * Ey) + (Et * Dy) / (2 * valC * D2 * Ey * Ey);

                    break;
                case Erl_Erl:
                    //TODO Написано в общем виде, не совсем идеально как в сатье, проверить с коэфициентами из статьи под графиком
                    Cf = (ErlErl.BETTA.getValue() * ErlErl.m.getValue() * ((2 + ErlErl.m.getValue()) * ErlErl.k.getValue() - 2 * ErlErl.m.getValue())) / (2 * ErlErl.ALPHA.getValue() * ErlErl.k.getValue() * (ErlErl.k.getValue() + ErlErl.m.getValue()) * valC);
                    Cs = (ErlErl.BETTA.getValue() * ErlErl.m.getValue() * (ErlErl.k.getValue() + 2 * ErlErl.m.getValue())) / (6 * ErlErl.ALPHA.getValue() * ErlErl.k.getValue() * (ErlErl.k.getValue() + ErlErl.m.getValue()) + valC);
                    break;
                case Par_Par:
                    Cf = ParPar.f.getValue() / valC;
                    Cs = ParPar.s.getValue() / valC;
                    break;
                case Par_Erl:
                    //FIXME расчет коэффициентов совпадает со статьей но рисует бред, разобраться
                    Cf = (ParErl.ay.getValue() - 2) * ParErl.betta.getValue() * ((Math.pow(ParErl.ay.getValue(), 2.0) * (-2 + ParErl.k.getValue() + 3 * Math.pow(ParErl.k.getValue(), 2.0)) -
                            ParErl.ay.getValue() * (-10 + 5 * ParErl.k.getValue() + Math.pow(ParErl.k.getValue(), 2.0)) + 6 * (-2 + ParErl.k.getValue())) /
                            (2 * (ParErl.ay.getValue() - 1) * (ParErl.ay.getValue() - 3) * ParErl.by.getValue() * ParErl.k.getValue() * Math.pow((-2 + ParErl.ay.getValue() + ParErl.ay.getValue() * ParErl.k.getValue()), 2.0) * valC));
                    Cs = -ParErl.betta.getValue() * ((Math.pow(ParErl.ay.getValue(), 3.0) * (2 + 3 * ParErl.k.getValue() + Math.pow(ParErl.k.getValue(), 2.0)) -
                            Math.pow(ParErl.ay.getValue(), 2.0) * (14 + 15 * ParErl.k.getValue() + 7 * Math.pow(ParErl.k.getValue(), 2.0)) + 2 * ParErl.ay.getValue() * (16 + 9 * ParErl.k.getValue() + 2 * Math.pow(ParErl.k.getValue(), 2.0)) - 24) /
                            (6 * (ParErl.ay.getValue() - 1) * (ParErl.ay.getValue() - 3) * ParErl.by.getValue() * ParErl.k.getValue() * Math.pow((-2 + ParErl.ay.getValue() + ParErl.ay.getValue() * ParErl.k.getValue()), 2.0) * valC));
                    break;
            }


            double Ft = (-(valC * valC * D2) / (u + valC * v)) * ((tmp1 + tmp2) - (tmp3 + tmp4)) + 2 * (1 - valC * M) * (tmp2 - tmp4) -
                    (((2 * valC * Math.sqrt(D2)) / Math.sqrt(Math.PI * x1 * (u + valC * v))) * ((Math.exp(((-(u + valC * v)) / (2 * x2 * valC * valC * D2)) * Math.pow(x2 * (1 - valC * M) - 1, 2.0))
                            - (Math.exp(((-(u + valC * v)) / (2 * x1 * valC * valC * D2)) * Math.pow(x1 * (1 - valC * M) - 1, 2.0))))));

            double St = (-(3 * valC * valC * D2) / (u + valC * v)) * ((tmp1 + tmp2) - (tmp3 + tmp4)) + 2 * (1 - valC * M) * (3 - 4 * ((u + valC * v) / (valC * valC * D2)) * (1 - valC * M)) * (tmp2 - tmp4) -
                    (((Math.sqrt(2.0) * valC * Math.sqrt(D2)) / (Math.sqrt(Math.PI * (u + valC * v)) * Math.pow(x2, 1.5)) * (3 * (1 - ((u + valC * v) / (valC * valC * D2)) * (1 - valC * M)) * x2 + (u + valC * v) / (valC * valC * D2))) * ((Math.exp(((-(u + valC * v)) / (2 * x2 * valC * valC * D2)) * Math.pow(x2 * (1 - valC * M) - 1, 2.0))))
                            - ((Math.sqrt(2.0) * valC * Math.sqrt(D2)) / (Math.sqrt(Math.PI * (u + valC * v)) * Math.pow(x1, 1.5)) * (3 * (1 - ((u + valC * v) / (valC * valC * D2)) * (1 - valC * M)) * x1 + (u + valC * v) / (valC * valC * D2))) * ((Math.exp(((-(u + valC * v)) / (2 * x1 * valC * valC * D2)) * Math.pow(x1 * (1 - valC * M) - 1, 2.0)))));

            outEt.put(valC, (tmp + Cf * Ft + Cs * St));
            c = c.add(incC);
        }
        return outEt;
    }
}

package sample;

import sample.Distributions.ExpExp;
import sample.Distributions.Type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.TreeMap;

public class Simulation {
    static ArrayList<Double> T = new ArrayList<>();
    static ArrayList<Double> Y = new ArrayList<>();

    public static void randomator(ArrayList<Double> T, ArrayList<Double> Y, double t, Type nameMethod, double c, double u) {
        Random random = new Random();
        double sumT = 0;
        double sumY = 0;
        int k = 0;
        double firstValue = 0.0;
        while ((sumY - sumT * c < u) && (t > sumT)) {

            switch (nameMethod) {
                case Exp_Exp:
                    T.add((-1 / ExpExp.betta.getValue()) * Math.log(1 - random.nextDouble()));
                    Y.add((-1 / ExpExp.alpha.getValue()) * Math.log(1 - random.nextDouble()));
                    break;
                case Par_MixExp:
                    double p = 2.0 / 3.0;
                    double q = 1 - p;
                    Y.add((1.0 / 0.35) * ((1.0 / Math.pow(1.0 - random.nextDouble(), 1.0 / 4.0)) - 1.0));
                    T.add(-Math.log((-p + Math.sqrt(p * p + 4.0 * q * (1.0 - random.nextDouble()))) / (2.0 * q)));
                    break;
                case Erl_Erl:
                    Y.add(-(1.0 / 1.0) * Math.log(random.nextDouble() * random.nextDouble()));
                    T.add(-(1.0 / 1.2) * Math.log(random.nextDouble() * random.nextDouble()));
                    break;
                case Par_Erl:
                    Y.add((1.0 / 0.4) * ((1.0 / Math.pow(1.0 - random.nextDouble(), 1.0 / 4.0)) - 1.0));
                    T.add(-(2.0 / 6.0) * (Math.log(random.nextDouble() * random.nextDouble())));
                    break;
                case Par_Par:
                    Y.add((1.0 / 0.4) * ((1.0 / Math.pow(1.0 - random.nextDouble(), 1.0 / 4.0)) - 1.0));
                    T.add((1.0 / 0.4) * ((1.0 / Math.pow(1.0 - random.nextDouble(), 1.0 / 4.0)) - 1.0));
                    break;
            }

            if ((sumY - (sumT * c)) > u) break;
            sumT += T.get(k);
            sumY += Y.get(k);
            k++;
        }
        T.set(0, 0.0);
    }

    public static boolean crossing(TreeMap<Integer, LinkedHashMap<Double, Double>> Beams, ArrayList<Double> T, ArrayList<Double> Y, double u, double c, double t, double cForBeams, Type nameMethod) {
        T.clear();
        Y.clear();
        randomator(T, Y, t, nameMethod, c, u);
        boolean result = false;
        double Tsum = 0;
        double Ysum = 0;

        if (c == cForBeams && Beams.size() < 50) {
            int j = Beams.size();
            LinkedHashMap<Double, Double> treeMap = new LinkedHashMap<>();
            treeMap.put(Y.get(0), 0.0 );
            double sumT = 0;
            double sumY = Y.get(0);
            int count = 1;
            while (((sumY - sumT * c) < u) && sumT < t && T.size()>count) {
                sumT += T.get(count);
                if (sumT > t) break;
                treeMap.put(sumY - c * sumT, sumT);
                sumY += Y.get(count);
                treeMap.put(sumY - c * sumT, sumT);
                count++;
            }
            Beams.put(j, treeMap);
        }

        for (Double v : T) {
            Tsum += v;
        }
        for (Double v : Y) {
            Ysum += v;
        }
        result = (Ysum - Tsum * c) > u;

        return result;
    }

    public static void simulation(TreeMap<Double, Double> pSimulation, TreeMap<Integer, LinkedHashMap<Double, Double>> Beams, double c, double u, double t, double beamsCount, double cForBeams, Type nameMethod) {
        int countCross = 0;

        for (int i = 0; i < beamsCount; i++) {
            if (crossing(Beams, T, Y, u, c, t, cForBeams, nameMethod)) countCross++;
        }

        pSimulation.put(c, countCross / beamsCount);
    }
}

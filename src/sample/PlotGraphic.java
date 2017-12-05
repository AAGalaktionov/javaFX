package sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class PlotGraphic {
    public static String plotOne = "ListLinePlot[ { %s }, GridLines -> Automatic, AxesLabel -> {c, T}]"; //, PlotStyle -> (Orange)]");
    public static String plotTwo = "ListLinePlot[ {%s, %s }, GridLines -> Automatic, AxesLabel -> {c, T}]";
    public static String plotThree = "ListLinePlot[ { %s, %s ,%s }, GridLines -> Automatic, AxesLabel -> {c, T}]";
    public static String plotBeams = "ListLinePlot[ { %s }, GridLines -> Automatic, AxesLabel -> {T, Y-c*T}]";


    public static void plot(String exec, boolean beamsPlot) {

        byte[] gifData = KernelConnection.getKernelLink().evaluateToImage(exec, 919, 718);
        try {
            FileOutputStream s;
            if (beamsPlot)
                s = new FileOutputStream(new File("test2.jpeg"));
            else
                s = new FileOutputStream(new File("test.jpeg"));
            s.write(gifData);
            s.close();
        } catch (IOException e) {
        }
    }

    public static String toMathFormat(Map<Double, Double> treeMap) {
        DecimalFormat df = new DecimalFormat("#.###");
        StringBuilder stringBuilder = new StringBuilder("{");
        for (HashMap.Entry<Double, Double> doubleDoubleEntry : treeMap.entrySet()) {
            stringBuilder.append("{" + doubleDoubleEntry.getKey() + "," + df.format(doubleDoubleEntry.getValue().doubleValue()).replace(',', '.') + "},");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static String toMathFormatBeams(Map<Double, Double> treeMap) {
        StringBuilder stringBuilder = new StringBuilder("{");
        for (HashMap.Entry<Double, Double> doubleDoubleEntry : treeMap.entrySet()) {
            stringBuilder.append("{" + doubleDoubleEntry.getValue() + "," + doubleDoubleEntry.getKey() + "},");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static String beamsPloatString(Map<Integer, LinkedHashMap<Double, Double>> Beams) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (HashMap.Entry<Integer, LinkedHashMap<Double, Double>> entry : Beams.entrySet()) {
            stringBuilder.append(toMathFormatBeams(entry.getValue())).append(" ,");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

}

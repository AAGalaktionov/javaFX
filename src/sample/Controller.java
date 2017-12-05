package sample;

import com.wolfram.jlink.MathLinkException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.Distributions.*;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class Controller implements Initializable {
    @FXML
    private Label label;
    @FXML
    private TextField c_end;
    @FXML
    private TextField c_begin;
    @FXML
    private TextField c_delta;
    @FXML
    private TextField u_0;
    @FXML
    private TextField t_0;
    private static ToggleGroup groupY;
    private static ToggleGroup groupT;
    @FXML
    private RadioButton yExp;
    @FXML
    private RadioButton yErl;
    @FXML
    private RadioButton yPar;
    @FXML
    private RadioButton tExp;
    @FXML
    private RadioButton tErl;
    @FXML
    private RadioButton tPar;
    @FXML
    private RadioButton tMix;

    @FXML
    private Label errorLabel;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea path;
    @FXML
    private CheckBox plotMt;
    @FXML
    private CheckBox plotBeams;
    @FXML
    private TextField c_beams;

    private static TreeMap<Double, Double> Mt = new TreeMap<>();
    private static TreeMap<Double, Double> Et = new TreeMap<>();
    private static TreeMap<Double, Double> Simulate = new TreeMap<>();
    private static TreeMap<Integer, LinkedHashMap<Double, Double>> Beams = new TreeMap<>();


    @FXML
    private void mouseOver(ActionEvent event) {
        imageView.setVisible(false);
        errorLabel.setText("");
    }

    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        //PlotGraphic.plot("Plot[BesselJ[0, x], {x, 0, 100}]");
        errorLabel.setText("");

        if (KernelConnection.getKernelLink() == null)
            try {
                KernelConnection.init(path.getText());
            } catch (MathLinkException e) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Некорректные параметры подлючения\n пакета Mathematica!");
                imageView.setVisible(false);
                KernelConnection.closeConnection();
                return;
            }

        final BigDecimal cBegin, cDelta, cEnd;
        final double u, t;
        double cForBeams = 0;

        try {
            cBegin = new BigDecimal(c_begin.getText());
            cDelta = new BigDecimal(c_delta.getText());
            cEnd = new BigDecimal(c_end.getText());
            u = Double.parseDouble(u_0.getText());
            t = Double.parseDouble(t_0.getText());
            if (plotBeams.isSelected()) {
                cForBeams = Double.parseDouble(c_beams.getText());
                if (cForBeams < cBegin.doubleValue() || cForBeams > cEnd.doubleValue())
                    throw new RuntimeException();
            }

        } catch (Exception e) {
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText("Некорректные входные данные!");
            imageView.setVisible(false);
            return;

        }
        if (yExp.equals(groupY.getSelectedToggle()) && tExp.equals(groupT.getSelectedToggle())) {
            work(u, t, cBegin, cDelta, cEnd, cForBeams, Type.Exp_Exp, plotMt.isSelected(), plotBeams.isSelected());
            return;
        }

        if (yErl.equals(groupY.getSelectedToggle()) && tErl.equals(groupT.getSelectedToggle())) {
            work(u, t, cBegin, cDelta, cEnd, cForBeams, Type.Erl_Erl, plotMt.isSelected(), plotBeams.isSelected());
            return;
        }

        if (yPar.equals(groupY.getSelectedToggle()) && tMix.equals(groupT.getSelectedToggle())) {
            work(u, t, cBegin, cDelta, cEnd, cForBeams, Type.Par_MixExp, plotMt.isSelected(), plotBeams.isSelected());
            return;
        }

        if (yPar.equals(groupY.getSelectedToggle()) && tErl.equals(groupT.getSelectedToggle())) {
            work(u, t, cBegin, cDelta, cEnd, cForBeams, Type.Par_Erl, plotMt.isSelected(), plotBeams.isSelected());
            return;
        }

        if (yPar.equals(groupY.getSelectedToggle()) && tPar.equals(groupT.getSelectedToggle())) {
            work(u, t, cBegin, cDelta, cEnd, cForBeams, Type.Par_Par, plotMt.isSelected(), plotBeams.isSelected());
            return;
        }
        imageView.setVisible(false);
        errorLabel.setTextFill(Color.BLUE);
        errorLabel.setText("Choosen distributions may be:\n" +
                "Exponential Y – Exponential T\n" +
                "Erlang Y – Erlang T\n" +
                "Pareto Y – Mixture of two Exponentials T\n" +
                "Pareto Y – Erlang T\n" +
                "Pareto Y – Pareto T");


    }

    private void printImage() {
        File file = new File("test.jpeg");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        imageView.setVisible(true);
    }

    private void work(double u, double t, BigDecimal cBegin, BigDecimal cDelta, BigDecimal cEnd, double cForBeams, Type type, boolean needEt, boolean needBeams) throws IOException {
        Mt.clear();
        Et.clear();
        Simulate.clear();
        Beams.clear();
        double M = 0, D2 = 0;

        switch (type) {
            case Exp_Exp:
                M = ExpExp.M.getValue();
                D2 = ExpExp.D2.getValue();
                break;
            case Par_MixExp:
                M = ParMixExp.M.getValue();
                D2 = ParMixExp.D2.getValue();
                break;
            case Erl_Erl:
                M = ErlErl.M.getValue();
                D2 = ErlErl.D2.getValue();
                break;
            case Par_Erl:
                M = ParErl.M.getValue();
                D2 = ParErl.D2.getValue();
                break;
            case Par_Par:
                M = ParPar.M.getValue();
                D2 = ParPar.D2.getValue();
                break;
        }

        BigDecimal c = cBegin;
        double v = 0.0;
        while (cEnd.compareTo(c) >= 0) {
            Simulation.simulation(Simulate, Beams, c.doubleValue(), u, t, 2000, cForBeams, type);
            c = c.add(cDelta);
        }
        Mt = Calculations.calcM(u, t, cBegin, cDelta, cEnd, v, M, D2);
        Et = Calculations.calcE(u, t, cBegin, cDelta, cEnd, v, M, D2, type);
        if (needEt)
            PlotGraphic.plot(String.format(PlotGraphic.plotThree, PlotGraphic.toMathFormat(Mt), PlotGraphic.toMathFormat(Simulate), PlotGraphic.toMathFormat(Et)), false);
        else
            PlotGraphic.plot(String.format(PlotGraphic.plotTwo, PlotGraphic.toMathFormat(Mt), PlotGraphic.toMathFormat(Simulate)), false);
        if (plotBeams.isSelected()) {
            PlotGraphic.plot(String.format(PlotGraphic.plotBeams, PlotGraphic.beamsPloatString(Beams)), true);
            Stage secondStage = new Stage();
            secondStage.setTitle("Beams");
            secondStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("beams.fxml"))));
            secondStage.setResizable(false);
            secondStage.show();
        }
        printImage();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        groupY = new ToggleGroup();
        yExp.setToggleGroup(groupY);
        yErl.setToggleGroup(groupY);
        yPar.setToggleGroup(groupY);
        groupT = new ToggleGroup();
        tErl.setToggleGroup(groupT);
        tExp.setToggleGroup(groupT);
        tMix.setToggleGroup(groupT);
        tPar.setToggleGroup(groupT);

    }
}

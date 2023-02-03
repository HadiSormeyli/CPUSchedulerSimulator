package com.example.cpuschedulersimulator.process.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class GantChartCell extends Label {


    public GantChartCell(int jobNo, Color color) {
        setMaxWidth(48);
        setMaxHeight(80);

        setPadding(new Insets(8, 8, 8, 8));
        HBox.setMargin(this, new Insets(2, 2, 2, 2));

        if (jobNo == 0 || jobNo == -1) {
            setStyle("-fx-background-color: #ffffff;");
            setText(" ");
        } else {
            setText(String.valueOf(jobNo));
            setStyle("-fx-background-color: " + rgbToHex(color) + ";");
        }
        alignmentProperty().set(Pos.CENTER);
    }

    public static String rgbToHex(Color color) {
        String hex = String.format("#%02X%02X%02X%02X"
                , (int) (color.getRed() * 100)
                , (int) (color.getGreen() * 100)
                , (int) (color.getBlue() * 100)
                , (int) (color.getOpacity() * 100));
        hex = hex.toUpperCase();
        return hex;
    }

}

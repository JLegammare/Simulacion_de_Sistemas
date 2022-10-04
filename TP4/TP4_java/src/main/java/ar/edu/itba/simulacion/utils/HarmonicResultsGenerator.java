package ar.edu.itba.simulacion.utils;

import ar.edu.itba.simulacion.models.Pair;

import java.io.*;
import java.util.List;

public class HarmonicResultsGenerator {

    private final File positionFile;

    public HarmonicResultsGenerator(String positionFilePath, String resultsDirectory) {

        File directory = new File(resultsDirectory);
        directory.mkdir();


        File positionFile = new File(positionFilePath);
        if(positionFile.exists())
            positionFile.delete();
        this.positionFile = positionFile;

    }

    public void addStateToPositionFile(List<Double> times, List<Pair<Double,Double>> positions) throws IOException {

        FileWriter fwPosition = new FileWriter(positionFile,true);
        BufferedWriter bwPosition = new BufferedWriter(fwPosition);
        PrintWriter pwPosition = new PrintWriter(bwPosition);

        StringBuilder sb = new StringBuilder();

        sb.append("time x y\n");

        int timeSize = times.size();
        for (int i = 0; i < timeSize; i++) {
            Pair<Double,Double> currentPos = positions.get(i);
            sb.append(String.format("%f %f %f\n",times.get(i),currentPos.getX_value(),currentPos.getY_value()));
        }
        pwPosition.print(sb);
        pwPosition.close();
    }

    public void addError(List<Double> dt, List<Double> errors) throws IOException {
        FileWriter fwPosition = new FileWriter(positionFile,true);
        BufferedWriter bwPosition = new BufferedWriter(fwPosition);
        PrintWriter pwPosition = new PrintWriter(bwPosition);

        StringBuilder sb = new StringBuilder();
        sb.append("dt error\n");
        int size = dt.size();
        for (int i = 0; i < size; i++) {

            sb.append(String.format("%f %s\n",dt.get(i),Double.toString(errors.get(i))));
        }
        pwPosition.print(sb);
        pwPosition.close();
    }


}

package ar.edu.itba.simulacion.utils;

import ar.edu.itba.simulacion.models.Body;
import ar.edu.itba.simulacion.models.Pair;

import java.io.*;
import java.util.List;
import java.util.Map;

public class PlanetsResultsGenerator {
    private final File dynamicFile;
    private final File staticFile;

    public PlanetsResultsGenerator(String dynamicFilePath, String staticFilePath, String resultsDirectory) {

        String dynamicResultsFilePath = String.format("%s/%s", resultsDirectory, dynamicFilePath);
        String staticResultsFilePath = String.format("%s/%s", resultsDirectory, staticFilePath);

        File directory = new File(resultsDirectory);
        directory.mkdir();

        File dymFile = new File(dynamicResultsFilePath);
        if(dymFile.exists())
            dymFile.delete();
        this.dynamicFile= dymFile;

        File stcFile = new File(staticResultsFilePath);
        if(stcFile.exists())
            stcFile.delete();
        this.staticFile= stcFile;

    }

    public void fillStaticFile(List<Body> bodies) throws IOException {

        FileWriter fw = new FileWriter(staticFile,false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n",bodies.size()));
        for (Body b: bodies) {
            sb.append(String.format("%d %2.2f %2.2f\n",b.getId(), b.getRadius(), b.getMass()));
        }
        pw.println(sb);
        pw.close();
    }



    public void addStateToDynamicFile(Map<Body,List<Pair<Double,Double>>> rsMap, double time ) throws IOException {

        FileWriter fwDynamic = new FileWriter(dynamicFile,true);
        BufferedWriter bwDynamic = new BufferedWriter(fwDynamic);
        PrintWriter pwDynamic = new PrintWriter(bwDynamic);

        StringBuilder sb = new StringBuilder();

//        sb.append(String.format("%f\n",time));
        sb.append(String.format("%d\na\n", rsMap.keySet().size()));
        // sol 1000
        // venus 200
        // tierra 500
        // spaceship 100
        rsMap.forEach((k,v)->{
            sb.append(String.format("%d %4.4f %4.4f %d %4.4f %4.4f %4.4f %d %d %d\n",
                    k.getId(),
                    v.get(0).getX_value(),
                    v.get(0).getY_value(),
                    0,
                    v.get(1).getX_value(),
                    v.get(1).getY_value(),
                    getRadius(k.getName()),
                    k.getBodyColor().getRed(),
                    k.getBodyColor().getGreen(),
                    k.getBodyColor().getBlue()
                    ));
        });

        pwDynamic.print(sb);
        pwDynamic.close();
    }

    private double getRadius(String bodyName){

        switch (bodyName){

            case "VENUS":
                return 300;
            case "SUN":
                return 1100;
            case "SPACESHIP":
                return 150;
            case "EARTH":
                return 450;
        }

        return 0;

    }

}

package ar.edu.itba.simulacion.utils;

import ar.edu.itba.simulacion.models.Body;

import java.io.*;
import java.util.List;

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



    public void addStateToDynamicFile(List<Body> bodies, double time ) throws IOException {

        FileWriter fwDynamic = new FileWriter(dynamicFile,true);
        BufferedWriter bwDynamic = new BufferedWriter(fwDynamic);
        PrintWriter pwDynamic = new PrintWriter(bwDynamic);

        StringBuilder sb = new StringBuilder();

//        sb.append(String.format("%f\n",time));
        sb.append(String.format("%d\na\n", bodies.size()));

        for (Body b: bodies) {
            sb.append(String.format("%d %4.4f %4.4f %4.4f %4.4f %4.4f %d %d %d\n",
                    b.getId(),
                    b.getPosition().getX_value(),
                    b.getPosition().getY_value(),
                    b.getVelocity().getX_value(),
                    b.getVelocity().getY_value(),
                    b.getRadius(),
                    b.getBodyColor().getRed(),
                    b.getBodyColor().getBlue(),
                    b.getBodyColor().getGreen()
                    ));

        }

        pwDynamic.print(sb);
        pwDynamic.close();
    }


}

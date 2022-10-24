package ar.edu.itba.simulacion.utils;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ResultsGenerator {
    private final File dynamicFile;
    private final File staticFile;

    public ResultsGenerator(String dynamicFilePath, String staticFilePath, String resultsDirectory) {

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

    public void fillStaticFile(List<Particle> bodies) throws IOException {

        FileWriter fw = new FileWriter(staticFile,false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n",bodies.size()));
        for (Particle particle: bodies) {
            sb.append(String.format("%d %2.2f %2.2f\n",particle.getID(), particle.getRadius(), particle.getMass()));
        }
        pw.println(sb);
        pw.close();
    }



    public void addStateToDynamicFile(Map<Particle,List<Pair<Double,Double>>> rsMap, double time ) throws IOException {

        FileWriter fwDynamic = new FileWriter(dynamicFile,true);
        BufferedWriter bwDynamic = new BufferedWriter(fwDynamic);
        PrintWriter pwDynamic = new PrintWriter(bwDynamic);

        StringBuilder sb = new StringBuilder();

//        sb.append(String.format("%f\n",time));
        sb.append(String.format("%d\na\n", rsMap.keySet().size()));

        rsMap.forEach((k,v)->{
            sb.append(String.format("%d %4.4f %4.4f %d %4.4f %4.4f %4.4f %d %d %d\n",
                    k.getID(),
                    v.get(0).getX_value(),
                    v.get(0).getY_value(),
                    0,
                    v.get(1).getX_value(),
                    v.get(1).getY_value(),
                    k.getRadius(),
                    k.getParticleColor().getRed(),
                    k.getParticleColor().getGreen(),
                    k.getParticleColor().getBlue()
                    ));
        });

        pwDynamic.print(sb);
        pwDynamic.close();
    }


}

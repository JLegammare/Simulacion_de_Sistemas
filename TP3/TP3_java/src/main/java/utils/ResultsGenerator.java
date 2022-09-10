package utils;

import models.Particle;

import java.io.*;
import java.util.List;

public class ResultsGenerator {

    private final File dynamicFile;
    private final File staticFile;

    public ResultsGenerator(String dynamicFilePath,String staticFilePath, String resultsDirectory) {

        File directory = new File(resultsDirectory);
        directory.mkdir();

        File dymFile = new File(dynamicFilePath);
        if(dymFile.exists())
            dymFile.delete();
        this.dynamicFile= dymFile;

        File stcFile = new File(staticFilePath);
        if(stcFile.exists())
            stcFile.delete();
        this.staticFile= stcFile;

    }

    public void fillStaticFile(List<Particle> particles, double l) throws IOException {

        FileWriter fw = new FileWriter(staticFile,false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n%f\n",particles.size(),l));
        for (Particle p: particles) {
            sb.append(String.format("%f %f\n",p.getRadius(), p.getProperty()));
        }
        pw.println(sb);
        pw.close();
    }


    public void addStateToDynamicFile(List<Particle> particles,double time ) throws IOException {

        FileWriter fw = new FileWriter(dynamicFile,true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

       
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\na\n",particles.size()));
        for (Particle p: particles) {
            sb.append(String.format("%d %f %f %f %f %f %f\n",p.getID(), p.getX(),p.getY(), 0f,p.getVelocityModule(),p.getVx(), p.getVy()));
        }
        pw.print(sb);
        pw.close();
    }


}

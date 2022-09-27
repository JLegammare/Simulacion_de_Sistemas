package utils;

import models.Particle;

import java.io.*;
import java.util.List;

public class ResultsGenerator {

    private final File dynamicFile;
    private final File staticFile;

    public ResultsGenerator(String dynamicFilePath, String staticFilePath, String particlePathFile, String collisionFile,String resultsDirectory) {

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
            sb.append(String.format("%f %f\n",p.getRadius(), p.getMass()));
        }
        pw.println(sb);
        pw.close();
    }



    public void addStateToDynamicFile(List<Particle> particles,double time ) throws IOException {

        FileWriter fwDynamic = new FileWriter(dynamicFile,true);
        BufferedWriter bwDynamic = new BufferedWriter(fwDynamic);
        PrintWriter pwDynamic = new PrintWriter(bwDynamic);


        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%f\n",time));

        for (Particle p: particles) {
            sb.append(String.format("%d %f %f %f %f \n",p.getID(),
                    p.getX(),
                    p.getY(),
                    p.getVx(),
                    p.getVy()
                    ));

        }

        pwDynamic.print(sb);
        pwDynamic.close();
    }


}

package utils;

import models.Collision;
import models.Particle;

import java.awt.*;
import java.io.*;
import java.util.List;

public class ResultsGenerator {

    private final File dynamicFile;
    private final File staticFile;
    private final File particleFile;
    private final File collisionFile;

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

        File partFile = new File(particlePathFile);
        if(partFile.exists())
            partFile.delete();
        this.particleFile = partFile;

        File colFile = new File(collisionFile);
        if(colFile.exists())
            colFile.delete();
        this.collisionFile = colFile;

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

    public void addBigParticleMovement(Particle bigParticle, double time) throws IOException {

        FileWriter fwParticle = new FileWriter(particleFile,true);
        BufferedWriter bwParticle = new BufferedWriter(fwParticle);
        PrintWriter pwParticle = new PrintWriter(bwParticle);

        pwParticle.println(String.format("%f %f %f ",time, bigParticle.getX(),bigParticle.getY()));
        pwParticle.close();

    }


    public void addStateToDynamicFile(List<Particle> particles,double time ) throws IOException {

        FileWriter fwDynamic = new FileWriter(dynamicFile,true);
        BufferedWriter bwDynamic = new BufferedWriter(fwDynamic);
        PrintWriter pwDynamic = new PrintWriter(bwDynamic);

        FileWriter fwCollision = new FileWriter(collisionFile,true);
        BufferedWriter bwCollision = new BufferedWriter(fwCollision);
        PrintWriter pwCollision = new PrintWriter(bwCollision);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%d\na\n",particles.size()));

        for (Particle p: particles) {
            Color particleColor = p.getParticleColor();
            sb.append(String.format("%d %f %f %f %f %f %f %f %d %d %d \n",p.getID(),
                    p.getX(),
                    p.getY(),
                    0f,
                    p.getVelocityModule(),
                    p.getVx(),
                    p.getVy(),
                    p.getRadius(),
                    particleColor.getRed(),
                    particleColor.getGreen(),
                    particleColor.getBlue()
                    ));

        }

        pwDynamic.print(sb);
        pwDynamic.close();
        pwCollision.println(String.format("%f",time));
        pwCollision.close();
    }


}

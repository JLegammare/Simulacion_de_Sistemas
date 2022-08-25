package utils;

import models.Particle;

import java.io.*;
import java.util.List;
import java.util.Map;

public class ResultsGenerator {

    private final File dynamicFile;
    private final File vaTimeFile;
    private final File staticFile;

    public ResultsGenerator(String dynamicFilePath,String vaFilePath, String staticFilePath, String resultsDirectory) {

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

        File vaFile = new File(vaFilePath);
        if(vaFile.exists())
            vaFile.delete();
        this.vaTimeFile=vaFile;

    }
//
//    public List<Particle> parseParticles(String staticFilePath, String dynamicFilePath) throws IOException {
//
//        File staticFile = new File(staticFilePath);
//        File dynamicFile = new File(dynamicFilePath);
//
//        Scanner staticScanner = new Scanner(staticFile);
//        Scanner dynamicScanner = new Scanner(dynamicFile);
//
//        int N = Integer.parseInt(staticScanner.next());
//        int L = Integer.parseInt(staticScanner.next());
//        ArrayList<Integer> times= new ArrayList<>();
//        times.add(dynamicScanner.nextInt());
//        List<Particle> particles = new ArrayList<>();
//
//        for (int j = 0; staticScanner.hasNextLine() && dynamicScanner.hasNextLine(); j++) {
//            float radius = staticScanner.nextFloat();
//            float property = staticScanner.nextFloat();
//            float posX = dynamicScanner.nextFloat();
//            float posY = dynamicScanner.nextFloat();
//            float maxRadius;
//            if(radius > maxRadius)
//                maxRadius = radius;
//            particles.add(new Particle(j,posX,posY,radius,property));
//        }
//
//        return particles;
//    }

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


    public void addStateToDynamicFile(List<Particle> particles,int time ) throws IOException {

        FileWriter fw = new FileWriter(dynamicFile,true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n",time));
        for (Particle p: particles) {
            sb.append(String.format("%d %f %f %f %f %f %f\n",p.getID(), p.getX(),p.getY(), 0f,p.getVelocityModule(),p.getXVelocity(), p.getYVelocity()));
        }
        pw.print(sb);
        pw.close();
    }
      public void generateVaTimeFile(Map<Integer,Double> orderParametersMap) throws IOException {

        FileWriter fw = new FileWriter(vaTimeFile,false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n",orderParametersMap.size()));
        orderParametersMap.forEach((key, value) -> sb.append(String.format("%d %f\n", key, value)));

        pw.print(sb);
        pw.close();
    }


}

package ar.edu.itba.simulacion.utils;

import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.Particle;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class ResultsGenerator {
    private final File dynamicFile;
    private final File staticFile;
    private final File timeFile;
    private final Color BORDER_COLOUR = Color.WHITE;
    private final double BORDER_RADIUS = 0.25;

    public ResultsGenerator(String dynamicFilePath, String staticFilePath,String timeFilePath, String resultsDirectory) {

        String dynamicResultsFilePath = String.format("%s/%s", resultsDirectory, dynamicFilePath);
        String staticResultsFilePath = String.format("%s/%s", resultsDirectory, staticFilePath);
        String timesResultsFilePath = String.format("%s/%s", resultsDirectory, timeFilePath);

        File directory = new File(resultsDirectory);
        directory.mkdir();

        File dymFile = new File(dynamicResultsFilePath);
        if (dymFile.exists())
            dymFile.delete();
        this.dynamicFile = dymFile;

        File stcFile = new File(staticResultsFilePath);
        if (stcFile.exists())
            stcFile.delete();
        this.staticFile = stcFile;

        File tmFile = new File(timesResultsFilePath);
        if (tmFile.exists())
            tmFile.delete();
        this.timeFile = tmFile;

    }

    public void fillStaticFile(List<Particle> bodies) throws IOException {

        FileWriter fw = new FileWriter(staticFile, false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d\n", bodies.size()));
        for (Particle particle : bodies) {
            sb.append(String.format("%d %f %f %f %f \n", particle.getID(), particle.getRadius(), particle.getMass(),
                    particle.getPosition().getX_value(),particle.getPosition().getY_value()));
        }
        pw.println(sb);
        pw.close();
    }


    public void addStateToDynamicFile(Map<Particle, List<Pair<Double, Double>>> rsMap,
                                      double W, double L, double D, double w, double A,
                                      double t) throws IOException {

        FileWriter fwDynamic = new FileWriter(dynamicFile, true);
        BufferedWriter bwDynamic = new BufferedWriter(fwDynamic);
        PrintWriter pwDynamic = new PrintWriter(bwDynamic);

        StringBuilder sb = new StringBuilder();

        int oneLateralParticles = (int) (L / (BORDER_RADIUS * 2)) + 1;
        int oneBottomWallParticles = (int) ((W - D) / (BORDER_RADIUS * 2) / 2);

        int totalBorderParticles = oneLateralParticles * 2 + oneBottomWallParticles * 2;

        sb.append(String.format("%d\na\n", rsMap.keySet().size() + totalBorderParticles));

        rsMap.forEach((k, v) -> {
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

        double posWall = A * sin(w * t);
        double speedWall = A * w * cos(w * t);

        //paredes laterales
        for (int i = 0; i < oneLateralParticles; i++) {
            sb.append(String.format("%d %4.4f %4.4f %d %4.4f %4.4f %4.4f %d %d %d\n",
                    900 + 2 * i,
                    0 - BORDER_RADIUS,
                    L - i * BORDER_RADIUS * 2 + posWall,
                    0,
                    0.0,
                    speedWall,
                    BORDER_RADIUS,
                    BORDER_COLOUR.getRed(),
                    BORDER_COLOUR.getGreen(),
                    BORDER_COLOUR.getBlue()
            ));
            sb.append(String.format("%d %4.4f %4.4f %d %4.4f %4.4f %4.4f %d %d %d\n",
                    900 + (2 * i + 1),
                    W + BORDER_RADIUS,
                    L - i * BORDER_RADIUS * 2 + posWall,
                    0,
                    0.0,
                    speedWall,
                    BORDER_RADIUS,
                    BORDER_COLOUR.getRed(),
                    BORDER_COLOUR.getGreen(),
                    BORDER_COLOUR.getBlue()
            ));
        }

        for (int i = 0; i < oneBottomWallParticles; i++) {
            sb.append(String.format("%d %4.4f %4.4f %d %4.4f %4.4f %4.4f %d %d %d\n",
                    1500 + 2 * i,
                    0 + i * BORDER_RADIUS * 2,
                    0 - BORDER_RADIUS + posWall,
                    0,
                    0.0,
                    speedWall,
                    BORDER_RADIUS,
                    BORDER_COLOUR.getRed(),
                    BORDER_COLOUR.getGreen(),
                    BORDER_COLOUR.getBlue()
            ));
            sb.append(String.format("%d %4.4f %4.4f %d %4.4f %4.4f %4.4f %d %d %d\n",
                    1500 + (2 * i + 1),
                    W - i * BORDER_RADIUS * 2,
                    0 - BORDER_RADIUS + posWall,
                    0,
                    0.0,
                    speedWall,
                    BORDER_RADIUS,
                    BORDER_COLOUR.getRed(),
                    BORDER_COLOUR.getGreen(),
                    BORDER_COLOUR.getBlue()
            ));
        }


        pwDynamic.print(sb);
        pwDynamic.close();
    }

    public void addTimeToFile(double time) throws IOException {

        FileWriter fw = new FileWriter(timeFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);

        pw.println(time);
        pw.close();
    }

}

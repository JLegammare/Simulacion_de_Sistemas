package ar.edu.itba.simulacion;

import ar.edu.itba.simulacion.models.Body;
import ar.edu.itba.simulacion.models.Pair;
import ar.edu.itba.simulacion.models.State;
import ar.edu.itba.simulacion.models.TripResult;
import ar.edu.itba.simulacion.utils.Parser;
import ar.edu.itba.simulacion.utils.PlanetsResultsGenerator;

import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.util.*;
import java.util.List;

public class VenusTripRunner {

    private static final String ASSETS_DIRECTORY = "assets/ByDay";
    private static final String EARTH_FILE_PATH = String.format("%s/earth.txt", ASSETS_DIRECTORY);
    private static final String VENUS_FILE_PATH = String.format("%s/venus.txt", ASSETS_DIRECTORY);
    private static final String RESULTS_DIRECTORY = "simulation_results/Venus_Mission/VenusTrip/ByDay";
    private static final String DYNAMIC_FILE = "Dynamic.txt";
    private static final String STATIC_FILE = "Static.txt";
    private static final String MIN_DISTANCE_FILE  = "MinDistance.txt";
    private static final double DT = 300;
    private static final double TF = 31557600;
    private static final Pair<Double, Double> sunPosition = new Pair<>(0.0, 0.0);
    private static final Pair<Double, Double> sunVelocity = new Pair<>(0.0, 0.0);
    private static final double SUN_RADIUS = 696000;
    private static final double SUN_MASS = 1988500E+24;
    //EARTH INPUT VALUES:
    private static final double EARTH_RADIUS = 6378.137;
    private static final double EARTH_MASS = 5.97219E+24;
    //VENUS INPUT VALUES:
    private static final double VENUS_RADIUS = 6051.84;
    private static final double VENUS_MASS = 48.685E+23;
    private static final Color SUN_COLOR = Color.yellow;
    private static final Color EARTH_COLOR = Color.BLUE;
    private static final Color VENUS_COLOR = Color.RED;

    public static void main(String[] args) throws IOException, ParseException {

            Map<Date, Pair<State, State>> states = Parser.parseBodies(EARTH_FILE_PATH, VENUS_FILE_PATH, ASSETS_DIRECTORY);

            Body sun = new Body(0, "SUN", sunPosition, sunVelocity, SUN_RADIUS, SUN_MASS,SUN_COLOR);
            List<TripResult> resultsMap = new ArrayList<>();

            states.forEach((d, p) -> {

                Body venus = new Body(1, "VENUS", p.getY_value().getPosition(), p.getY_value().getVelocity(),
                        VENUS_RADIUS, VENUS_MASS,VENUS_COLOR);
                Body earth = new Body(2, "EARTH", p.getX_value().getPosition(), p.getX_value().getVelocity(),
                        EARTH_RADIUS, EARTH_MASS,EARTH_COLOR);

                String directory = String.format("%s/%s",RESULTS_DIRECTORY, d.toString());


                PlanetsResultsGenerator rg = new PlanetsResultsGenerator(DYNAMIC_FILE,STATIC_FILE,directory);
                try {
                    List<Body> bodies = new ArrayList<>();

                    bodies.add(sun);
                    bodies.add(venus);
                    bodies.add(earth);
                    System.out.printf("Running %s\n", d);
                    TripResult tr = VenusTrip.venusTripMethod(rg,bodies,DT,TF,d);
                    resultsMap.add(tr);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            persistMinDistances(resultsMap,String.format("%s/%s",RESULTS_DIRECTORY,MIN_DISTANCE_FILE));
            TripResult min = resultsMap.stream().min(Comparator.comparingDouble(TripResult::getDistanceTraveled)).get();
            System.out.printf("***** MIN ******:\n\tDATE:%s\tDISTANCE:%f\tTIME:%f%n",min.getStartDate(),min.getMinDistance(),min.getTime());
    }

    private static void persistMinDistances(List <TripResult> resultsList,String minDistanceFilePath) throws IOException {

        File minDistanceFile = new File(minDistanceFilePath);
        if(minDistanceFile.exists())
            minDistanceFile.delete();

        FileWriter fw = new FileWriter(minDistanceFile,false);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        StringBuilder sb = new StringBuilder();

        resultsList.forEach((r)-> {
            sb.append(String.format("%s,%f\n",r.getStartDate(),r.getMinDistance()));
        });

        pw.print(sb);
        pw.close();

    }

}

package ar.edu.itba.simulacion.utils;

import ar.edu.itba.simulacion.models.State;
import ar.edu.itba.simulacion.models.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Parser {

    private static final String DELIMITER = "$$SOE";
    private static final String FINAL_DELIMITER = "$$EOE";
    private static final String DATE_FORMAT = "yyyy-MMM-dd HH:mm:ss.SSSS";


    public static Map<Date, Pair<State, State>> parseBodies(String earthFilePath, String venusFilePath, String directory) throws FileNotFoundException, ParseException {

        File earthFile = new File(earthFilePath);
        File venusFile = new File(venusFilePath);

        Map<Date, Pair<State, State>> datePairMap = new TreeMap<>();

        List<Pair<Date, State>> venusStates = parseStates(venusFile);
        List<Pair<Date, State>> earthStates = parseStates(earthFile);

        int statesAmount = earthStates.size();

        for (int i = 0; i <statesAmount ; i++) {

            Pair<Date,State> earthSt = earthStates.get(i);
            Pair<Date,State> venusSt = venusStates.get(i);

            datePairMap.put(earthSt.getX_value(),new Pair<>(earthSt.getY_value(),venusSt.getY_value()));

        }
        return datePairMap;
    }

    private static List<Pair<Date, State>> parseVenus(File venusFile) {
        return null;
    }

    private static List<Pair<Date, State>> parseStates(File earthFile) throws FileNotFoundException, ParseException {

        List<Pair<Date,State>> list = new ArrayList<>();

        Scanner earthScanner = new Scanner(earthFile);
        boolean firstDelimiter = false;
        while (earthScanner.hasNextLine()) {

            String line = earthScanner.nextLine();
            if (firstDelimiter) {
                if (line.equals(FINAL_DELIMITER))
                    break;
                else {
                    String dateLine = line.substring(line.lastIndexOf("A.D.")+5,line.length()-5);
                    SimpleDateFormat sdf  = new SimpleDateFormat(DATE_FORMAT,Locale.US);
                    Date date = sdf.parse(dateLine);

                    String positionLine = earthScanner.nextLine();
                    double x = Double.parseDouble(
                            positionLine.substring( positionLine.lastIndexOf("X")+3,
                                    positionLine.lastIndexOf("Y")-1));
                    double y = Double.parseDouble(
                            positionLine.substring( positionLine.lastIndexOf("Y")+3,
                                    positionLine.lastIndexOf("Z")-1));

                    String velocityLine = earthScanner.nextLine();
                    double vx = Double.parseDouble(
                            velocityLine.substring( velocityLine.lastIndexOf("VX")+3,
                                    velocityLine.lastIndexOf("Y")-1));
                    double vy = Double.parseDouble(
                            velocityLine.substring( velocityLine.lastIndexOf("VY")+3,
                                    velocityLine.lastIndexOf("Z")-1));


                    Pair<Date,State> entry = new Pair<>(date,new State(new Pair<>(x,y),new Pair<>(vx,vy)));
                    list.add(entry);

                }
            } else if (line.equals(DELIMITER)) {
                firstDelimiter = true;
            }

        }

        return list;
    }


}


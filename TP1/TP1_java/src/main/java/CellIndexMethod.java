import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CellIndexMethod {
    //N: cantidad de particulas
    private static int N = 10;
    //N_ITERATIONS: cantidad de iteraciones en el tiempo
    private static final int N_ITERATIONS = 1;
//    R: radio de la particula
    private static final double R= 0.37;
//    L: largo de una celda
    private static int L;
//    M: Cantidad de celdas del board cuadrado (MxM)
    private static final int M= 6;
//    RC: Radio de interaccion
    private static final int RC = 5;
    private static final String STATIC_FILE = "TP1/TP1_java/assets/Static100.txt";
    private static final String DYNAMIC_FILE = "TP1/TP1_java/assets/Dynamic100.txt";
    private static final String OUTPUT_FILE = "TP1/TP1_java/assets/Output.txt";

    public static void main(String[] args) throws IOException {

        List<Particle> particles = Parser.parseParticles(STATIC_FILE,DYNAMIC_FILE);
        double boardLength = L*M;

        Map<Particle,List<Particle>> neighborsMap = new HashMap<>();

        for (Particle p: particles) {
            neighborsMap.put(p,particles.stream().filter(particle ->
                particle.checkIfNeighbor(p,RC)
            ).collect(Collectors.toList()));
        }

       neighborsMap.forEach((i,l)->{
            System.out.println(l);
       });

       generateOutput(neighborsMap);
    }

    public static void generateOutput(Map<Particle,List<Particle>> neighbors) throws IOException {
        FileWriter fw = new FileWriter(OUTPUT_FILE);
        BufferedWriter bw = new BufferedWriter(fw);


        neighbors.forEach((particle, particles) -> {
        });

        bw.flush();
        bw.close();

}

      public static void setL(int l) {
        L = l;
    }

    public static void setN(int n) {
        N = n;
    }
}

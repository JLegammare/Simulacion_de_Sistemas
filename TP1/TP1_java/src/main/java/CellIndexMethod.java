import java.util.*;
import java.util.stream.Collectors;

public class CellIndexMethod {
    //N: cantidad de particulas
    private static final int N = 10;
    //N_ITERATIONS: cantidad de iteraciones en el tiempo
    private static final int N_ITERATIONS = 1;
//    R: radio de la particula
    private static final double R= 0.37;
//    L: largo de una celda
    private static final int L= 6;
//    M: Cantidad de celdas del board cuadrado (MxM)
    private static final int M= 6;
//    RC: Radio de interaccion
    private static final int RC = 5;

    public static void main(String[] args) {

        List<Particle> particles = new ArrayList<>();
        double boardLength = L*M;

        for (int i = 0; i < N; i++)
            particles.add(new Particle(i,boardLength,R));

        Map<Particle,List<Particle>> neighborsMap = new HashMap<>();

        for (Particle p: particles) {
            neighborsMap.put(p,particles.stream().filter(particle ->
                particle.checkIfNeighbor(p,RC)
            ).collect(Collectors.toList()));
        }

       neighborsMap.forEach((i,l)->{
            System.out.println(l);
        });

    }

    public void generateOutput(Map<Particle,List<Particle>> neighbors){

    }



}

import java.util.ArrayList;
import java.util.List;

public class CellIndexMethod {

    private static final int N_PARTICLES = 1000;
    private static final int N_ITERATIONS = 1;
    private static final int SIDE_LENGTH = 6;
    private static final int M = 30;
    private static final int INTERACTION_RADIUS = 5;

    public static void main(String[] args) {

        List<Particle> particles = new ArrayList<>();

        for (int i = 0; i < N_PARTICLES; i++)
            particles.add(new Particle(SIDE_LENGTH));

        dynamicInputGeneration(particles);
        staticInputGeneration(particles);

    }

    private static void staticInputGeneration( List<Particle> particles ){

    }
    private static void dynamicInputGeneration(List<Particle> particles){

    }


}

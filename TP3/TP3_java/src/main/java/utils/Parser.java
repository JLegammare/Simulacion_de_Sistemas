package utils;

import org.apache.commons.cli.*;

public class Parser {

    public CommandLine parseArguments(String[] args){
        Options options = new Options();

        Option l = new Option("l", "length", true, "length of the cells");
        l.setRequired(true);
        options.addOption(l);

        Option np = new Option(
                "np",
                "particles_number",
                true,
                "number of particles to be created");
        np.setRequired(true);
        options.addOption(np);

        Option r1 = new Option(
                "rs",
                "small_particle_radius",
                false,
                "small particles radius");
        r1.setRequired(true);
        options.addOption(r1);

        Option r2 = new Option(
                "rb",
                "particle_radius_2",
                false,
                "big particle radius");
        r2.setRequired(true);
        options.addOption(r2);

        Option m2 = new Option(
                "mb",
                "particle_mass_2",
                false,
                "big particle mass");
        m2.setRequired(true);
        options.addOption(m2);

        Option m1 = new Option(
                "ms",
                "particle_mass_2",
                false,
                "small particle mass");
        m1.setRequired(true);
        options.addOption(m1);


        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try{
            cmd = parser.parse(options,args);

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name",options);
            throw new RuntimeException(e);
        }

        return cmd;
    }


}

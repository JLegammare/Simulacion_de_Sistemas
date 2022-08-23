package utils;

import org.apache.commons.cli.*;

public class Parser {

    public CommandLine parseArguments(String[] args){
        Options options = new Options();

        Option n = new Option("n", "noise", true, "noise parameter of the simulation");
        n.setRequired(true);
        options.addOption(n);

        Option l = new Option("l", "length", true, "length of the cells");
        l.setRequired(true);
        options.addOption(l);

        Option rc = new Option("rc", "i_radius", true, "particle interaction radius");
        rc.setRequired(true);
        options.addOption(rc);

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

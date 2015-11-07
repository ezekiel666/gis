package pl.zerocool.gis.generator;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ParserProperties;
import pl.zerocool.gis.generator.algs4.Digraph;
import pl.zerocool.gis.generator.algs4.DigraphGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Spam detector main class.
 *
 * @author Cezary Pawlowski
 */
public class Main {
    /** Logger. */
    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    /**
     * Gets options parser.
     *
     * @param options options
     * @return a options parser
     */
    private static CmdLineParser getParser(Options options) {
        ParserProperties parserProperties = ParserProperties.defaults();
        parserProperties.withOptionValueDelimiter("=");
        parserProperties.withUsageWidth(80);
        return new CmdLineParser(options, parserProperties);
    }

    private static Options parseArguments(String[] args) {
        Options options = new Options();
        CmdLineParser parser = getParser(options);

        try {
            parser.parseArgument(args);
        } catch(CmdLineException e) {
            System.out.println(e.getMessage() + "!");
            System.out.println("See '-help'.");
            System.exit(0);
        }

        if(options.help) {
            printHelp(parser);
            System.exit(0);
        }

        if(!options.validate()) {
            System.out.println("Invalid parameter value!");
            System.out.println("See '-help'.");
            System.exit(0);
        }

        return options;
    }

    private static void setUpLogger(Options options) {
        Level level;
        if(options.logLevel == null) {
            level = Level.OFF;
        } else {
            level = Level.parse(options.logLevel.toUpperCase(Locale.ENGLISH));
        }

        Logger root = Logger.getLogger("");
        root.setLevel(level);
        for(Handler handler : root.getHandlers()) {
            handler.setLevel(level);
        }

        LOGGER.config("logging configured");

        /*
        LOGGER.severe("test");
        LOGGER.warning("test");
        LOGGER.info("test");
        LOGGER.config("test");
        LOGGER.fine("test");
        LOGGER.finer("test");
        LOGGER.finest("test");
        */
    }

    /**
     * Main entry.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        Options options = parseArguments(args);
        setUpLogger(options);

        /* program flow */

        try {
            Digraph graph = null;
            if(options.simpleMode) {
                LOGGER.info("simple mode");
                graph = DigraphGenerator.simple(options.v, options.e);
            } else if(options.probMode) {
                LOGGER.info("probabilistic mode");
                graph = DigraphGenerator.simple(options.v, options.p);
            } else if(options.strongMode) {
                LOGGER.info("strong mode");
                graph = DigraphGenerator.strong(options.v, options.e, options.c);
            }

            LOGGER.info("converting graph");
            DirectedGraph jgraph = convert(graph);

            LOGGER.info("saving graph");
            save(jgraph, options.outputFile);

        } catch(Exception e) {
            System.out.println("Critical error occurred: " + e);
            return;
        }
    }

    private static DirectedGraph convert(Digraph graph) {
        DirectedGraph<Integer, DefaultEdge> jgraph =
                new DefaultDirectedGraph<>(DefaultEdge.class);

        for(Integer v1 = 0; v1 < graph.V(); ++v1) {
            jgraph.addVertex(v1);
        }

        for(Integer v1 = 0; v1 < graph.V(); ++v1) {
            for(Integer v2 : graph.adj(v1)) {
                jgraph.addEdge(v1, v2);
            }
        }

        return jgraph;
    }

    private static void save(DirectedGraph jgraph, String outputFile) throws FileNotFoundException, UnsupportedEncodingException {
        DOTExporter exporter = new DOTExporter();
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
        exporter.export(writer, jgraph);
    }

    /**
     * Prints help.
     *
     * @param parser options parser
     */
    private static void printHelp(CmdLineParser parser) {
        System.out.println("Directed graph generator developed for GIS classes by Cezary Pawlowski and Pawel Banasiak.");
        System.out.println();
        System.out.println("Usage:");
        parser.printUsage(System.out);
        System.out.println();
        System.out.println("Program mode needs to be specified explicitly.");
        System.out.println("Generated graphs are saved in DOT (plain text graph description language).");
    }
}

package pl.zerocool.gis.tarjan;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTImporter;
import org.jgrapht.ext.EdgeProvider;
import org.jgrapht.ext.ImportException;
import org.jgrapht.ext.VertexProvider;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ParserProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Program main class.
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
            LOGGER.info("reading graph");
            DirectedGraph<Integer, DefaultEdge> graph = read(options.inputFile);
            LOGGER.info("vertices: " + graph.vertexSet().size());
            LOGGER.info("edges: " + graph.edgeSet().size());

        } catch(Exception e) {
            System.out.println("Critical error occurred: " + e);
            return;
        }
    }

    /**
     * Reads graph from a file.
     *
     * @param inputFile input file with graph in dot language
     * @return a graph
     * @throws ImportException if an error occurs
     * @throws IOException if an error occurs
     */
    private static DirectedGraph<Integer, DefaultEdge> read(String inputFile) throws ImportException, IOException {
        DOTImporter<Integer, DefaultEdge> importer =
                new DOTImporter<>(new VertexProviderImpl(), new EdgeProviderImpl());

        DirectedGraph<Integer, DefaultEdge> graph =
                new DefaultDirectedGraph<>(DefaultEdge.class);

        String fileContent = new String(Files.readAllBytes(Paths.get(inputFile)), "UTF-8");

        importer.read(fileContent, (AbstractBaseGraph<Integer, DefaultEdge>) graph);

        return graph;
    }

    /**
     * Prints help.
     *
     * @param parser options parser
     */
    private static void printHelp(CmdLineParser parser) {
        System.out.println("Tarjan's strongly connected components algorithm developed for GIS classes by Cezary Pawlowski and Pawel Banasiak.");
        System.out.println();
        System.out.println("Usage:");
        parser.printUsage(System.out);
    }

    /**
     * Vertex provider.
     *
     * @author Cezary Pawlowski
     */
    public static class VertexProviderImpl implements VertexProvider<Integer> {
        @Override
        public Integer buildVertex(String label, Map<String, String> attributes) {
            return Integer.parseInt(label);
        }
    }

    /**
     * Edge provider.
     *
     * @author Cezary Pawlowski
     */
    public static class EdgeProviderImpl implements EdgeProvider<Integer, DefaultEdge> {
        @Override
        public DefaultEdge buildEdge(Integer from, Integer to, String label, Map<String, String> attributes) {
            DefaultEdge edge = new DefaultEdge();
            edge.setSource(from);
            edge.setTarget(to);
            return edge;
        }
    }
}

package pl.zerocool.gis.generator;

import org.kohsuke.args4j.Option;

import java.util.Arrays;
import java.util.Locale;

/**
 * Options.
 *
 * @author Cezary Pawlowski
 */
public class Options {
    @Option(name = "-simpleMode", usage = "simple mode - generates random digraph with v vertices and e edges",
            depends = {"-e"}, forbids = {"-probMode", "-strongMode", "-p", "-c"})
    public boolean simpleMode;

    @Option(name = "-probMode", usage = "probabilistic mode - generates random digraph with v vertices and probablity p of edge between any two vertices being connected (Erdos-Renyi random digraph model)",
            depends = {"-p"}, forbids = {"-simpleMode", "-strongMode", "-e", "-c"})
    public boolean probMode;

    @Option(name = "-strongMode", usage = "strong mode - generates random digraph with v vertices, e edges and (maximum) c strong components",
            depends = {"-e", "-c"}, forbids = {"-simpleMode", "-probMode", "-p"})
    public boolean strongMode;

    @Option(name = "-v", usage = "specifies number of vertices [0, +inf)", required = true)
    public Integer v;

    @Option(name = "-e", usage = "specifies number of edges [0, +inf)")
    public Integer e;

    @Option(name = "-c", usage = "specifies (maximum) number of strong components [0, +inf)")
    public Integer c;

    @Option(name = "-p", usage = "specifies probability of edge between any two vertices being connected [0, 1]")
    public Double p;

    @Option(name = "-outputFile", usage = "specifies result file with generated graph (.gv is preferable extension)", required = true)
    public String outputFile;

    @Option(name = "-logLevel", usage = "specifies log level [off (def), severe, warning, info, config, fine, finer, finest, all]")
    public String logLevel;

    @Option(name = "-help", usage = "prints help", help = true)
    public boolean help;

    /**
     * Validates values range.
     *
     * @return true if validation is successful
     */
    public boolean validate() {
        return ((simpleMode || probMode || strongMode)
                && (v >= 0)
                && (e == null || e >= 0)
                && (c == null || c >= 0)
                && (p == null || (p >= 0 && p <= 1))
                && (logLevel == null || Arrays.asList(new String[] {"OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL"}).contains(logLevel.toUpperCase(Locale.ENGLISH))));
    }
}

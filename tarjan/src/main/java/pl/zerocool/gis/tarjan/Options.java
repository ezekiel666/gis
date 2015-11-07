package pl.zerocool.gis.tarjan;

import org.kohsuke.args4j.Option;

import java.util.Arrays;
import java.util.Locale;

/**
 * Options.
 *
 * @author Cezary Pawlowski
 */
public class Options {
    @Option(name = "-inputFile", usage = "specifies input file with graph (dot language)", required = true)
    public String inputFile;

    @Option(name = "-time", usage = "indicates if time measuring info should be displayed")
    public boolean time;

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
        return (logLevel == null || Arrays.asList(new String[] {"OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL"}).contains(logLevel.toUpperCase(Locale.ENGLISH)));
    }
}

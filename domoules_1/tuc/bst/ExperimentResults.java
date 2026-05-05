

/**
 * Stores the 4 measurement groups for one input size N.
 */
public class ExperimentResults {

    Metrics insertMetrics;
    Metrics deleteMetrics;
    Metrics searchMetrics;
    Metrics rangeMetrics;

    public ExperimentResults(Metrics insertMetrics,
                             Metrics deleteMetrics,
                             Metrics searchMetrics,
                             Metrics rangeMetrics) {
        this.insertMetrics = insertMetrics;
        this.deleteMetrics = deleteMetrics;
        this.searchMetrics = searchMetrics;
        this.rangeMetrics = rangeMetrics;
    }
}
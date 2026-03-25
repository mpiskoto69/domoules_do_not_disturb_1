

/**
 * Stores total times, operations, and levels for the 3 structures
 * for one operation and one value of N.
 */
public class Metrics {

    long time1, ops1, lev1;
    long time2, ops2, lev2;
    long time3, ops3, lev3;
    int K;

    public Metrics(long time1, long ops1, long lev1,
                   long time2, long ops2, long lev2,
                   long time3, long ops3, long lev3,
                   int K) {
        this.time1 = time1;
        this.ops1 = ops1;
        this.lev1 = lev1;

        this.time2 = time2;
        this.ops2 = ops2;
        this.lev2 = lev2;

        this.time3 = time3;
        this.ops3 = ops3;
        this.lev3 = lev3;

        this.K = K;
    }
}
public class PercolationStats {
    private Percolation pr;
    private double[] fraction; // array to install percentage of each experiment
    private int times; // times = T
    
    public PercolationStats(int N, int T) {    // perform T independent experiments on an N-by-N grid
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("invalid input");
        times = T;
        fraction = new double[times];
        for (int expe = 0; expe < times; expe ++) {
            pr = new Percolation(N);
            int count = 0;
            while (!pr.percolates()) {
                int i = StdRandom.uniform(1, N + 1);
                int j = StdRandom.uniform(1, N + 1);
                if (!pr.isOpen(i, j)) {
                    pr.open(i, j);
                    count ++;
                }
            }
            fraction[expe] = (double) count / (N * N);           
        }
    }
    public double mean() {                     // sample mean of percolation threshold
        return StdStats.mean(fraction);
    }
    
    public double stddev() {                   // sample standard deviation of percolation threshold
        if (times == 1)
            return Double.NaN;
        return StdStats.stddev(fraction);
    }
    
    public double confidenceLo() {             // low  endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / (Math.sqrt(times));
    }
    
    public double confidenceHi() {             // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / (Math.sqrt(times));
    }

    public static void main(String[] args) {   // test client (described below)
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(N, T);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + ps.confidenceLo() +", " + ps.confidenceHi());
        
    }
    
}
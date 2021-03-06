public class Percolation {
    private WeightedQuickUnionUF phase; // status of the grid
    private int side; // side = N, to be used in all methods
    private boolean[] states; // states[i] = component identifier of i in the grid
                              // 0:false: blocked
                              // 1:true: open
    
    public Percolation(int N) { // create N-by-N grid, with all sites blocked
        if (N <= 0) 
            throw new IllegalArgumentException("invalid input"); 
        states = new boolean[N * N + 2]; // 2 come from 2 virtual sites out of the grid
        states[0] = true;
        states[N * N + 1] = true;
        phase = new WeightedQuickUnionUF(N * N + 2);
        side = N;
    }
    
    private void checkRange(int i, int j) {
        if (i <= 0 || i > side || j <= 0 || j > side)
            throw new IndexOutOfBoundsException("index out of range");
    }
    
    private int xyTo1D(int i, int j) { // map 2D index (i, j) to 1D array of length N^2
        return (i - 1) * side + j;
    }
 
    public void open(int i, int j) {         // open site (row i, column j) if it is not open already
        checkRange(i, j);
        if (isOpen(i, j))
            return;
        states[xyTo1D(i, j)] = true;
        if (i == 1)
            phase.union(xyTo1D(i, j), 0);
        if (i == side)
            phase.union(xyTo1D(i, j), side * side + 1);
        if (i > 1 && isOpen(i - 1, j))
            phase.union(xyTo1D(i, j), xyTo1D(i - 1, j));
        if (i < side && isOpen(i + 1, j))
            phase.union(xyTo1D(i, j), xyTo1D(i + 1, j));
        if (j > 1 && isOpen(i, j - 1))
            phase.union(xyTo1D(i, j), xyTo1D(i, j - 1));
        if (j < side && isOpen(i, j + 1))
            phase.union(xyTo1D(i, j), xyTo1D(i, j + 1));
    }

    public boolean isOpen(int i, int j) {    // is site (row i, column j) open?
        checkRange(i, j);
        return states[xyTo1D(i, j)];
    }
    
    public boolean isFull(int i, int j) {    // is site (row i, column j) full?
        checkRange(i, j);
        return phase.connected(0, xyTo1D(i, j));
    }
    
    public boolean percolates() {             // does the system percolate?
        return phase.connected(0, side * side + 1);
    }
    
    public static void main(String[] args) { // test client (optional)
        int N = 10;
        Percolation uf = new Percolation(N);
        System.out.println("initial count is " + uf.phase.count());
        uf.open(3, 1);
        System.out.println("after 1st open, is it open ?" + uf.isOpen(3, 1));
        System.out.println("is 1st full: " + uf.isFull(3, 1));
        System.out.println("count after 1st open ? " + uf.phase.count());
        System.out.println("is 2nd open ? " + uf.isOpen(2, 2));
        uf.open(2, 2);
        System.out.println("after open 2nd, is it open? " + uf.isOpen(2, 2));
        System.out.println("count after 2nd open: " + uf.phase.count());
        System.out.println("1st and 2nd connected? " + uf.phase.connected(uf.xyTo1D(2, 1), uf.xyTo1D(2, 2)));
        System.out.println("2nd full? " + uf.isFull(2, 2));
    }
}


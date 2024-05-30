import java.io.*;
import java.util.*;


public class PrimeFinder {

    static final int LIMIT = (int) Math.pow(10, 7);

    private static ArrayList<Long> generatePrimes(long limit) {

        BitSet prime = new BitSet((int) limit + 1);
        prime.set(2, (int) limit + 1);

        for (int i = 2; i <= limit; i++) {
            if (prime.get(i)) {
                for (int j = i * 2; j <= limit; j += i)
                    prime.clear(j);
            }
        }

        ArrayList<Long> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (prime.get(i)) primes.add((long) i);
        }

        return primes;
    }

    private static ArrayList<Long> segmentSieve(long low, long high, String fileName, int bufferSize) throws IOException {

        BitSet mark = new BitSet((int) (high - low + 1));
        mark.set(0, (int) (high - low + 1));

        long filePosition = 0;
        ArrayList<Long> primes = readFromFile(fileName, bufferSize, filePosition);
        ArrayList<Long> newPrimes = new ArrayList<>();

        for (Long prime : primes) {

            if (prime.equals(0L)) break;

            long start = Math.max(prime * prime, ((low + prime - 1) / prime) * prime);
            for (long j = start; j <= high; j += prime) {
                mark.clear((int) (j - low));
            }

            long limit = low + bufferSize > high ? high : low + bufferSize;
            for (long i = low; i <= limit; i++) {
                if (mark.get((int) (i - low))) {
                    newPrimes.add(i);
                    filePosition++;
                }
            }

            low += bufferSize;

            if ((low - 1) % bufferSize == 0) {
                primes = readFromFile(fileName, bufferSize, filePosition);
            }
        }

        return newPrimes;
    }

    private static ArrayList<Long> segmentSieve(long low, long high, ArrayList<Long> primes) {

        BitSet mark = new BitSet((int) (high - low + 1));
        mark.set(0, (int) (high - low + 1));

        for (long prime : primes) {
            long start = Math.max(prime * prime, ((low + prime - 1) / prime) * prime);
            for (long j = start; j <= high; j += prime) {
                mark.clear((int) (j - low));
            }
        }

        ArrayList<Long> newPrimes = new ArrayList<>();
        for (long i = low; i <= high; i++) {
            if (mark.get((int) (i - low))) newPrimes.add(i);
        }

        return newPrimes;
    }

    public static ArrayList<Long> readFromFile(String fileName, int limit, long position) throws IOException {

        ArrayList<Long> primes = new ArrayList<>();
        RandomAccessFile raf = new RandomAccessFile(fileName, "r");

        raf.seek(position);
        String line;
        int count = 0;

        while ((line = raf.readLine()) != null && count < limit) {
            long prime = Long.parseLong(line);
            primes.add(prime);
            count++;
        }
        
        raf.close();
        return primes;
    }

    public static void writeToFile(ArrayList<Long> primes, String fileName) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(fileName), true));
            for (Long integer : primes) {
                printWriter.println(integer);
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void smallestPrimes(String fileName) throws IOException {
        long low =  LIMIT;
        long high = LIMIT * 2 - 1;

        ArrayList<Long> smallPrimes = generatePrimes(low - 1);
        writeToFile(smallPrimes, fileName);

        while (true) {
            ArrayList<Long> primes = segmentSieve(low, high, smallPrimes);
            low += LIMIT;
            high += LIMIT;
            writeToFile(primes, fileName);
            try {
                smallPrimes.addAll(primes);
            } catch (OutOfMemoryError e) {
                writeToFile(primes, fileName);
                while (true) {
                    ArrayList<Long> newPrimes = segmentSieve(low, high, fileName, 50000);
                    writeToFile(newPrimes, fileName);
                    low += LIMIT;
                    high += LIMIT;
                }
            }

        }
    }

    public static void main(String[] args) throws IOException {
        smallestPrimes("22103808d.txt");
    }
    
}
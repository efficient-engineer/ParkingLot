public class FeeCalculator {

    private static final int fph = 10;
    static double calculate(Ticket ticket) {
        Vehicle v = ticket.getV();
        long timeElapsedMillis = System.currentTimeMillis() - ticket.getTimestamp();
        int hour = Math.toIntExact(timeElapsedMillis / (1_0000 * 3600));
        return 10 * hour;
    }
}

import lombok.Getter;

public class ParkingSpot {
    @Getter
    private int id;

    @Getter
    private boolean isAvailable;

    @Getter
    private Vehicle v;

    public void setAvailability(boolean b) {
        isAvailable = b;
    }
}

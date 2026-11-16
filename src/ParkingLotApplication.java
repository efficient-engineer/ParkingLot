import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLotApplication {

    private final List<ParkingSpot> parkingSpots;
    private final Map<UUID, Ticket> ticketIdMap;

    public ParkingLotApplication(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
        ticketIdMap = new ConcurrentHashMap<>();
    }

    public Ticket park(Vehicle vehicleType) {
        ParkingSpot ps = tryToOccupyAvailableSpot(vehicleType);
        if (ps == null) {
            throw new IllegalStateException("No compatible parking spot available");
        }
        try {
            Ticket t = generateTicket(vehicleType, ps);
            ticketIdMap.put(t.getId(), t);
            return t;
        } catch (RuntimeException | Error e) {
            ps.release();
            throw e;
        }
    }

    private Ticket generateTicket(Vehicle vehicleType, ParkingSpot ps) {
        return new Ticket(vehicleType, ps.getId());
    }

    private ParkingSpot tryToOccupyAvailableSpot(Vehicle vehicleType) {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.tryOccupy(vehicleType)) {
                return spot;
            }
        }
        return null;
    }

    public double exit(UUID id) {
        Ticket ticket = ticketIdMap.remove(id);
        if(ticket == null) throw new RuntimeException("Invalid Ticket");
        setSpotToAvailable(ticket.getSpotId());
        return FeeCalculator.calculate(ticket);
    }

    private void setSpotToAvailable(int spotId) {
        for(ParkingSpot spot : parkingSpots) {
            if(spot.getId() == spotId) {
                spot.release();
                return;
            }
        }
    }
}

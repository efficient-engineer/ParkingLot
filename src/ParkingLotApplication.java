import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ParkingLotApplication {

    private final List<ParkingSpot> parkingSpots;
    private final Map<UUID, Ticket> ticketIdMap;

    public ParkingLotApplication(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
        ticketIdMap = new HashMap<>();
    }

    public Ticket park(Vehicle v) {
        ParkingSpot ps = getAvailableSpot(v);
        if (ps == null) {
            throw new RuntimeException("Couldn't find");
        }
        ps.setAvailability(false);
        Ticket t = generateTicket(v, ps);
        ticketIdMap.put(t.getId(), t);;
        return t;
    }

    private Ticket generateTicket(Vehicle v, ParkingSpot ps) {
        return new Ticket(v, ps.getId());
    }

    private ParkingSpot getAvailableSpot(Vehicle v) {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.getV().equals(v) && spot.isAvailable()) {
                return spot;
            }
        }
        return null;
    }

    public double exit(UUID id) {
        Ticket ticket = ticketIdMap.get(id);
        if(ticket == null) throw new RuntimeException("Invalid Ticket");
        setSpotToAvailable(ticket.getSpotId());
        ticketIdMap.remove(id);
        return FeeCalculator.calculate(ticket);
    }

    private void setSpotToAvailable(int spotId) {
        for(ParkingSpot spot : parkingSpots) {
            if(spot.getId() == spotId) {
                spot.setAvailability(true);
                return;
            }
        }
    }
}

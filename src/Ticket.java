import lombok.Getter;

import java.util.UUID;

@Getter
public class Ticket {

    private UUID id;
    private long timestamp;
    private Vehicle v;
    private int spotId;

    public Ticket(Vehicle v, int id) {
        this.id = UUID.randomUUID();
        this.v = v;
        this.timestamp = System.currentTimeMillis();
        this.spotId = id;
    }
}

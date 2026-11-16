import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ParkingSpot {
    @Getter
    private final int id;

    @Getter
    private final Vehicle supportedVehicleType;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean available = true;

    public ParkingSpot(int id, Vehicle supportedVehicleType) {
        this.id = id;
        this.supportedVehicleType = Objects.requireNonNull(supportedVehicleType, "supportedVehicleType");
    }

    public boolean isAvailable() {
        lock.readLock().lock();
        try {
            return available;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean tryOccupy(Vehicle vehicleType) {
        Objects.requireNonNull(vehicleType, "vehicleType");
        lock.writeLock().lock();
        try {
            if (!available || supportedVehicleType != vehicleType) {
                return false;
            }
            available = false;
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void release() {
        lock.writeLock().lock();
        try {
            available = true;
        } finally {
            lock.writeLock().unlock();
        }
    }
}

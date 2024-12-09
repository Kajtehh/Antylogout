package pl.kajteh.antylogout;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Combat {

    private final UUID opponent;
    private final Instant startTime;

    public Combat(UUID opponent) {
        this.opponent = opponent;
        this.startTime = Instant.now();
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public UUID getAnotherEntity() {
        return this.opponent;
    }

    public long getTimeLeft(Instant now, long combatDuration) {
        final long elapsedSeconds = Duration.between(this.startTime, now).getSeconds();

        return Math.max(0, combatDuration - elapsedSeconds);
    }

    public boolean hasElapsed(Instant now, long combatDuration) {
        final long elapsedSeconds = Duration.between(this.startTime, now).getSeconds();

        return elapsedSeconds >= combatDuration;
    }
}

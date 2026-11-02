package de.laugenbreze.dungeonsalive.settlement.inn;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

/** Implemented by the real owner-bound anchor BlockEntity, never by a client message. */
public interface InnAnchorAccess {
    UUID ownerId();
    @Nullable UUID innId();
    void bindInn(UUID id);
}

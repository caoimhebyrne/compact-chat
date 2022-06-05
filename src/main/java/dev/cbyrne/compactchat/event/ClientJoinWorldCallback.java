package dev.cbyrne.compactchat.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Fired whenever the client 'joins' a new world. This can be either an internal (single-player) or external (multiplayer) world.
 * @see net.minecraft.client.MinecraftClient#joinWorld(net.minecraft.client.world.ClientWorld)
 */
public interface ClientJoinWorldCallback {
    Event<ClientJoinWorldCallback> EVENT = EventFactory.createArrayBacked(ClientJoinWorldCallback.class,
        (listeners -> () -> {
            for (var listener : listeners) {
                listener.onJoin();
            }
        })
    );

    void onJoin();
}

/*
 * This file is part of Almura.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.almura.feature.exchange.network.handler;

import com.almuradev.almura.feature.exchange.ServerExchangeManager;
import com.almuradev.almura.feature.exchange.network.ServerboundExchangeSpecificOfferRequestPacket;
import com.almuradev.almura.shared.util.PacketUtil;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.network.MessageHandler;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.network.RemoteConnection;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class ServerboundExchangeSpecificOfferPacketHandler implements MessageHandler<ServerboundExchangeSpecificOfferRequestPacket> {

    private final ServerExchangeManager exchangeManager;

    @Inject
    public ServerboundExchangeSpecificOfferPacketHandler(final ServerExchangeManager exchangeManager) {
        this.exchangeManager = exchangeManager;
    }

    @Override
    public void handleMessage(final ServerboundExchangeSpecificOfferRequestPacket message, final RemoteConnection connection,
        final Platform.Type side) {
        if (side.isServer() && connection instanceof PlayerConnection && PacketUtil
            .checkThreadAndEnqueue((MinecraftServer) Sponge.getServer(), message, this, connection, side)) {

            final Player player = ((PlayerConnection) connection).getPlayer();

            this.exchangeManager.handleExchangeSpecificOffer(player, message.id);
        }
    }
}

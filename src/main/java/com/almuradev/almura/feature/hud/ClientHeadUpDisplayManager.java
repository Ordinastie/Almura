/*
 * This file is part of Almura.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.almura.feature.hud;

import com.almuradev.almura.core.client.config.ClientConfiguration;
import com.almuradev.almura.feature.hud.screen.AbstractHUD;
import com.almuradev.almura.feature.hud.screen.origin.OriginHUD;
import com.almuradev.core.event.Witness;
import com.almuradev.toolbox.config.map.MappedConfiguration;
import com.google.inject.Injector;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@SideOnly(Side.CLIENT)
@Singleton
public class ClientHeadUpDisplayManager implements Witness {

    private final Injector injector;
    private final MappedConfiguration<ClientConfiguration> configAdapter;
    private final HeadUpDisplay hudData;

    @Nullable private AbstractHUD hud;

    @Inject
    public ClientHeadUpDisplayManager(final Injector injector, final MappedConfiguration<ClientConfiguration> configAdapter, final HeadUpDisplay hudData) {
        this.injector = injector;
        this.configAdapter = configAdapter;
        this.hudData = hudData;
    }

    @SubscribeEvent
    public void onRenderGameOverlayPre(final RenderGameOverlayEvent.Pre event) {
        switch (this.configAdapter.get().general.hud.toLowerCase()) {
            case HUDType.ORIGIN:
                if (!(this.hud instanceof OriginHUD)) {
                    if (this.hud != null) {
                        this.hud.closeOverlay();
                    }
                    this.hud = this.injector.getInstance(OriginHUD.class);
                    this.hud.displayOverlay();
                }
                switch (event.getType()) {
                    case AIR:
                    case ARMOR:
                    case BOSSHEALTH:
                    case BOSSINFO:
                    case DEBUG:
                    case EXPERIENCE:
                    case FOOD:
                    case HEALTH:
                    case HEALTHMOUNT:
                    case PLAYER_LIST:
                        event.setCanceled(true);
                        break;
                    default:
                }
                break;
            case HUDType.VANILLA:
            default:
                if (this.hud != null) {
                    this.hud.closeOverlay();
                    this.hud = null;
                }
                break;
        }
    }

    @SubscribeEvent
    public void onMouse(final MouseEvent event) {
        if (this.hud instanceof OriginHUD) {
            event.setCanceled(((OriginHUD) this.hud).handleScroll());
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.hudData.worldName = "";
        this.hudData.onlinePlayerCount = 0;
        this.hudData.maxPlayerCount = 0;
        this.hudData.isEconomyPresent = false;
        this.hudData.economyAmount = "";
    }

    @Nullable
    public AbstractHUD getHUDDirect() {
        return this.hud;
    }

    public Optional<AbstractHUD> getHUD() {
        return Optional.ofNullable(this.hud);
    }
}

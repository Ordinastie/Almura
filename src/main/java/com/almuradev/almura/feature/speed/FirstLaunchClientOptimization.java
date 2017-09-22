/*
 * This file is part of Almura, All Rights Reserved.
 *
 * Copyright (c) AlmuraDev <http://github.com/AlmuraDev/>
 */
package com.almuradev.almura.feature.speed;

import com.almuradev.almura.core.client.config.ClientCategory;
import com.almuradev.almura.core.client.config.ClientConfiguration;
import com.almuradev.shared.config.ConfigLoadEvent;
import com.almuradev.shared.event.Witness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FirstLaunchClientOptimization implements Witness {

    @SubscribeEvent
    public void configLoad(final ConfigLoadEvent<ClientConfiguration> event) {
        final ClientCategory category = event.config().client;
        if (category.firstLaunch) {
            category.firstLaunch = false;
            this.optimizeGame();
            event.adapter().save();
        }
    }

    /**
     * Called on first launch to optimize the client's GUI settings. Addresses many users lack of knowledge of
     * the various settings that can lead to better FPS. Improves overall experience with Almura.
     */
    private void optimizeGame() {
        // TODO Dockter, update your optimization changes
        final GameSettings settings = Minecraft.getMinecraft().gameSettings;
        settings.autoJump = false;
        settings.ambientOcclusion = 0;
        settings.mipmapLevels = 0;
        settings.guiScale = 3;
        //settings.advancedOpengl = true;
        //settings.anisotropicFiltering = 0;
        settings.limitFramerate = 120;
        settings.enableVsync = false;
        //settings.clouds = false;
        settings.snooperEnabled = false;
        settings.renderDistanceChunks = 12;
        settings.viewBobbing = false;
        settings.resourcePacks.add("Almura Preferred Font.zip");
        settings.saveOptions();
    }
}
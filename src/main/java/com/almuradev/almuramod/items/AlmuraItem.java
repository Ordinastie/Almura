/**
 * This file is part of AlmuraMod, All Rights Reserved.
 *
 * Copyright (c) 2014 AlmuraDev <http://github.com/AlmuraDev/>
 */
package com.almuradev.almuramod.items;

import com.almuradev.almuramod.AlmuraMod;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class AlmuraItem extends Item {
   
    /**
     * Create fake item for Creative Tabs images
     */
    public AlmuraItem(String name) {
        this(name, false, null, 1);
    }
    
    // Real Item Registry
    public AlmuraItem(String name, boolean showInCreativeTab, CreativeTabs creativeTabName, int maxStackSize) {
        if (showInCreativeTab) {
            setCreativeTab(creativeTabName);
        }

        this.setUnlocalizedName(name);
        setTextureName(AlmuraMod.MOD_ID + ":" + name);
        GameRegistry.registerItem(this, name);
    }
}
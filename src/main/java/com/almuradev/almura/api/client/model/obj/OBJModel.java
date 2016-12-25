/*
 * This file is part of Almura, All Rights Reserved.
 *
 * Copyright (c) AlmuraDev <http://github.com/AlmuraDev/>
 */
package com.almuradev.almura.api.client.model.obj;

import static com.google.common.base.Preconditions.checkState;

import com.almuradev.almura.api.client.model.obj.geometry.Group;
import com.almuradev.almura.api.client.model.obj.geometry.Vertex;
import com.almuradev.almura.api.client.model.obj.geometry.VertexNormal;
import com.almuradev.almura.api.client.model.obj.geometry.VertexTextureCoordinate;
import com.almuradev.almura.api.client.model.obj.material.MaterialDefinition;
import com.almuradev.almura.api.client.model.obj.material.MaterialLibrary;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.IModelState;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * While MinecraftForge does have an OBJLoader and OBJModel, this approach doesn't require changes on the modeller's part when exporting from
 * Blender, 3ds Max, etc and instead dynamically converts the model piped into a format Minecraft understands.
 */
public class OBJModel implements IRetexturableModel, IModelCustomData {

    private final ResourceLocation source;
    private final String name;
    private final MaterialLibrary materialLibrary;
    private final Set<Group> groups;
    private final Set<ResourceLocation> textures;

    private OBJModel(ResourceLocation source, String name, MaterialLibrary materialLibrary, Set<Group> groups) {
        this.source = source;
        this.name = name;
        this.materialLibrary = materialLibrary;
        this.groups = groups;
        this.textures = new HashSet<>();

        for (MaterialDefinition materialDefinition : materialLibrary.getMaterialDefinitions()) {

            final ResourceLocation diffuseLocation = materialDefinition.getDiffuseTexture().orElse(null);

            if (diffuseLocation == null) {
                continue;
            }

            if (diffuseLocation.getResourcePath().endsWith(".png")) {
                this.textures.add(new ResourceLocation(diffuseLocation.getResourceDomain(), diffuseLocation
                        .getResourcePath().split("\\.")[0]));
            } else {
                this.textures.add(diffuseLocation);
            }


        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData) {
        return null;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures) {
        return null;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return textures;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new OBJBakedModel(this, state, format, bakedTextureGetter);
    }

    @Override
    public IModelState getDefaultState() {
        return null;
    }

    public String getName() {
        return this.name;
    }

    public ResourceLocation getSource() {
        return this.source;
    }

    public MaterialLibrary getMaterialLibrary() {
        return this.materialLibrary;
    }

    public Set<Group> getGroups() {
        return this.groups;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("name", this.name)
                .add("source", this.source)
                .add("groups", this.groups)
                .toString();
    }

    public static final class Builder {

        private MaterialLibrary materialLibrary;
        private LinkedHashMap<Vertex, Integer> vertices = new LinkedHashMap<>();
        private LinkedHashMap<VertexNormal, Integer> normals = new LinkedHashMap<>();
        private LinkedHashMap<VertexTextureCoordinate, Integer> textureCoordinates = new LinkedHashMap<>();
        private Set<Group> groups = new LinkedHashSet<>();

        public Builder materialLibrary(MaterialLibrary materialLibrary) {
            this.materialLibrary = materialLibrary;
            return this;
        }

        public MaterialLibrary materialLibrary() {
            return this.materialLibrary;
        }

        public Builder vertex(Vertex vertex) {
            final int index = this.vertices.size() + 1;
            this.vertices.put(vertex, index);
            vertex.setIndex(index);
            return this;
        }

        public LinkedHashMap<Vertex, Integer> vertices() {
            return this.vertices;
        }

        public Builder normal(VertexNormal normal) {
            final int index = this.normals.size() + 1;
            this.normals.put(normal, index);
            normal.setIndex(index);
            return this;
        }

        public LinkedHashMap<VertexNormal, Integer> normals() {
            return this.normals;
        }

        public Builder textureCoordinate(VertexTextureCoordinate textureCoordinate) {
            final int index = this.textureCoordinates.size() + 1;
            this.textureCoordinates.put(textureCoordinate, index);
            textureCoordinate.setIndex(index);
            return this;
        }

        public LinkedHashMap<VertexTextureCoordinate, Integer> textureCoordinates() {
            return this.textureCoordinates;
        }

        public Builder group(Group group) {
            this.groups.add(group);
            return this;
        }

        public OBJModel build(ResourceLocation source, String name) {
            checkState(source != null, "Source cannot be null!");
            checkState(name != null, "Name cannot be null!");
            checkState(!name.isEmpty(), "Name cannot be empty!");
            checkState(!this.groups.isEmpty(), "Groups cannot be empty!");

            return new OBJModel(source, name, this.materialLibrary, this.groups);
        }
    }
}
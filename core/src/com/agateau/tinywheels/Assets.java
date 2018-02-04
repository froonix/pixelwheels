/*
 * Copyright 2017 Aurélien Gâteau <mail@agateau.com>
 *
 * This file is part of Pixel Wheels.
 *
 * Tiny Wheels is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.agateau.tinywheels;

import com.agateau.tinywheels.gameobjet.AnimationObject;
import com.agateau.tinywheels.map.MapInfo;
import com.agateau.tinywheels.sound.AudioManager;
import com.agateau.tinywheels.sound.SoundAtlas;
import com.agateau.tinywheels.vehicledef.VehicleDef;
import com.agateau.tinywheels.vehicledef.VehicleIO;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Stores all assets
 */
public class Assets {
    public static class UiAssets {
        public final Skin skin;
        public final TextureAtlas atlas;
        public final TextureRegion background;
        public final NinePatch selection;

        UiAssets() {
            this.atlas = new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas"));
            this.skin = new Skin(this.atlas);

            loadFonts();

            this.skin.load(Gdx.files.internal("ui/uiskin.json"));

            this.background = this.atlas.findRegion("background");
            this.selection = this.atlas.createPatch("focus");
        }

        private void loadFonts() {
            FreeTypeFontGenerator.FreeTypeFontParameter parameter;
            this.skin.add("default-font", loadFont("fonts/Xolonium-Regular.ttf", 28));
            this.skin.add("title-font", loadFont("fonts/Aero.ttf", 32));

            parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 12;
            parameter.borderWidth = 0.5f;
            this.skin.add("small-font", loadFont("fonts/Xolonium-Regular.ttf", parameter));

            parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 28;
            parameter.borderWidth = 0.5f;
            this.skin.add("hud-font", loadFont("fonts/Xolonium-Regular.ttf", parameter));

            parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 56;
            parameter.characters = "1234567890thsrdneméè";
            parameter.borderWidth = 0.5f;
            this.skin.add("hud-rank-font", loadFont("fonts/Xolonium-Regular.ttf", parameter));
        }

        private BitmapFont loadFont(String name, int size) {
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            return loadFont(name, parameter);
        }

        private BitmapFont loadFont(String name, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(name));
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            return font;
        }
    }

    private static final float EXPLOSION_FRAME_DURATION = 0.1f;
    private static final float IMPACT_FRAME_DURATION = 0.05f;
    private static final float MINE_FRAME_DURATION = 0.2f;
    private static final float TURBO_FRAME_DURATION = 0.1f;
    private static final float TURBO_FLAME_FRAME_DURATION = 0.04f;

    private static final String[] VEHICLE_IDS = { "red", "police", "pickup", "roadster", "antonin", "santa" };

    public final Array<VehicleDef> vehicleDefs = new Array<VehicleDef>();
    public final Array<MapInfo> mapInfos = new Array<MapInfo>(new MapInfo[]{
            new MapInfo("race", "Let it Snow"),
            new MapInfo("be", "City"),
            new MapInfo("tiny-sur-mer", "Tiny sur Mer"),
    });
    public final UiAssets ui = new UiAssets();

    public final TextureRegion wheel;
    public final TextureRegion dot;
    public final TextureAtlas atlas;
    public final Animation impact;
    public final Animation mine;
    public final Animation turbo;
    public final Animation turboFlame;
    public final Animation splash;
    public final TextureRegion gift;
    public final Animation gunAnimation;
    public final TextureRegion bullet;
    public final TextureRegion skidmark;
    public final TextureRegion helicopterBody;
    public final TextureRegion helicopterPropeller;
    public final TextureRegion helicopterPropellerTop;
    public final SoundAtlas soundAtlas = new SoundAtlas(Gdx.files.internal("sounds"));

    private final Animation explosion;
    private final HashMap<String, TextureAtlas.AtlasRegion> mRegions = new HashMap<String, TextureAtlas.AtlasRegion>();

    Assets() {
        if (GamePlay.instance.showTestTrack) {
            mapInfos.add(new MapInfo("test", "Test"));
        }

        this.atlas = new TextureAtlas(Gdx.files.internal("sprites/sprites.atlas"));
        this.wheel = findRegion("wheel");
        this.explosion = new Animation(EXPLOSION_FRAME_DURATION, this.findRegions("explosion"));
        this.impact = new Animation(IMPACT_FRAME_DURATION, this.findRegions("impact"));
        this.mine = new Animation(MINE_FRAME_DURATION, this.findRegions("mine"));
        this.mine.setPlayMode(Animation.PlayMode.LOOP);
        this.turbo = new Animation(TURBO_FRAME_DURATION, this.findRegions("bonus-turbo"));
        this.turboFlame = new Animation(TURBO_FLAME_FRAME_DURATION, this.findRegions("turbo-flame"));
        this.turboFlame.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.splash = new Animation(TURBO_FLAME_FRAME_DURATION, this.findRegions("splash"));
        this.gift = findRegion("gift");
        this.gunAnimation = new Animation(0.1f / 3, this.findRegions("bonus-gun"));
        this.bullet = findRegion("bullet");

        // Fix white-pixel to avoid fading borders
        this.dot = findRegion("white-pixel");
        removeBorders(this.dot);

        this.skidmark = findRegion("skidmark");

        this.helicopterBody = this.findRegion("helicopter-body");
        this.helicopterPropeller = this.findRegion("helicopter-propeller");
        this.helicopterPropellerTop = this.findRegion("helicopter-propeller-top");

        loadVehicleDefinitions();
        initSoundAtlas();
    }

    private void initSoundAtlas() {
        for (int i = 0; i < 5; ++i) {
            String name = String.format("engine-%d", i);
            String filename = String.format("loop_%d_0.wav", i + 1);
            this.soundAtlas.load(filename, name);
        }
        this.soundAtlas.load("drifting.wav");
        this.soundAtlas.load("bonus.wav");
        this.soundAtlas.load("explosion.wav");
        this.soundAtlas.load("shoot.wav");
        this.soundAtlas.load("impact.wav");
        this.soundAtlas.load("turbo.wav");
        this.soundAtlas.load("impact.wav", "collision");
    }

    public VehicleDef getVehicleById(String id) {
        for (VehicleDef def : this.vehicleDefs) {
            if (def.id.equals(id)) {
                return def;
            }
        }
        return null;
    }

    private static void removeBorders(TextureRegion region) {
        region.setRegionX(region.getRegionX() + 2);
        region.setRegionY(region.getRegionY() + 2);
        region.setRegionWidth(region.getRegionWidth() - 4);
        region.setRegionHeight(region.getRegionHeight() - 4);
    }

    public TextureAtlas.AtlasRegion findRegion(String name) {
        TextureAtlas.AtlasRegion region = mRegions.get(name);
        if (region != null) {
            return region;
        }
        region = this.atlas.findRegion(name);
        if (region == null) {
            throw new RuntimeException("Failed to load a texture region named '" + name + "'");
        }
        mRegions.put(name, region);
        return region;
    }

    public Array<TextureAtlas.AtlasRegion> findRegions(String name) {
        Array<TextureAtlas.AtlasRegion> lst = this.atlas.findRegions(name);
        if (lst.size == 0) {
            throw new RuntimeException("Failed to load an array of regions named '" + name + "'");
        }
        return lst;
    }

    public VehicleDef findVehicleDefByID(String id) {
        for (VehicleDef def : vehicleDefs) {
            if (def.id.equals(id)) {
                return def;
            }
        }
        return null;
    }

    public MapInfo findMapInfoByID(String id) {
        for (MapInfo info : mapInfos) {
            if (info.getId().equals(id)) {
                return info;
            }
        }
        return null;
    }

    public AnimationObject createExplosion(AudioManager audioManager, float x, float y) {
        AnimationObject obj = AnimationObject.create(explosion, x, y);
        obj.initAudio(audioManager, soundAtlas.get("explosion"));
        return obj;
    }

    private void loadVehicleDefinitions() {
        for (String id : VEHICLE_IDS) {
            this.vehicleDefs.add(VehicleIO.get(id));
        }
    }
}

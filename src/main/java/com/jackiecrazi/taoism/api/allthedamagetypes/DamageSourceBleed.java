package com.jackiecrazi.taoism.api.allthedamagetypes;

import net.minecraft.util.DamageSource;

public class DamageSourceBleed extends DamageSource {

    private DamageSourceBleed() {
        super("bleed");
        this.setDamageBypassesArmor();
        this.setDamageIsAbsolute();
        this.setMagicDamage();
    }

    public static DamageSource causeBleedingDamage(){
        return new DamageSourceBleed();
    }
}

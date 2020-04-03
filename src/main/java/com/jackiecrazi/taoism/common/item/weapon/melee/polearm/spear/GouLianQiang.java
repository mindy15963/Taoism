package com.jackiecrazi.taoism.common.item.weapon.melee.polearm.spear;

import com.jackiecrazi.taoism.api.NeedyLittleThings;
import com.jackiecrazi.taoism.api.PartDefinition;
import com.jackiecrazi.taoism.api.StaticRefs;
import com.jackiecrazi.taoism.capability.TaoCasterData;
import com.jackiecrazi.taoism.common.item.weapon.melee.TaoWeapon;
import com.jackiecrazi.taoism.utils.TaoCombatUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class GouLianQiang extends TaoWeapon {
    /*
    A spear weapon that specializes in hooking and grabbing. High range and speed, medium combo and defense, low power
    Reference: a guisarme can be used to catch incoming blades
    A guisarme can hook down an overhead parry for a stab
    A goulian qiang has a blunt hook used for pulling. It's mainly used to pull people off horses, pull people off balance or trip horses
    I imagine the cavity would lead to great catching of blades.
    Anyway this trips people and counters parries.

    left click for a normal stab. Doing this attack twice to the same target (first time has half cooldown) will grab and trip, inflicting (2+armor/5) posture damage
    right click to do a bash that knocks the target a fair distance away and inflicts blunt damage, at cost of lower damage
    These two attacks have independent cooldowns,so you can continuously chain them.
    TODO this loses the spear's ability to pierce. Instead, winning a blade lock will disable the opponent's weapon for 1 second.
    riposte:
    //the next bash in 4 seconds AoEs, knocks back and briefly slows the opponents
    //the next stab in 4 seconds instantly hooks and trips
     */

    public GouLianQiang() {
        super(2, 1.2, 4d, 1f);
    }

    @Override
    public PartDefinition[] getPartNames(ItemStack is) {
        return StaticRefs.SIMPLE;
    }

    @Override
    public float critDamage(EntityLivingBase attacker, EntityLivingBase target, ItemStack item) {
        float aerial = !attacker.onGround ? 1.5f : 1f;
        float bash = off ? 0.5f : 1f;
        return aerial * bash;
    }

    @Override
    public int getDamageType(ItemStack is) {
        return off ? 0 : 2;
    }

    @Override
    public int getComboLength(EntityLivingBase wielder, ItemStack is) {
        return isDummy(is) ? 1 : 2;
    }

    @Override
    public float newCooldown(EntityLivingBase elb, ItemStack is) {
        return getCombo(elb, is) != getComboLength(elb, is) - 1 ? 0.5f : 0f;
    }

    @Override
    public float getReach(EntityLivingBase p, ItemStack is) {
        return 5.5f;
    }

    @Override
    public int getMaxChargeTime() {
        return 80;
    }

    @Override
    public float postureMultiplierDefend(EntityLivingBase attacker, EntityLivingBase defender, ItemStack item, float amount) {
        return 1.4f;
    }

    @Override
    public boolean isTwoHanded(ItemStack is) {
        return true;
    }

    protected void aoe(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, int chi) {
        if (off && isCharged(attacker, stack))
            splash(attacker, target, 2f);
        else if (!off)
            splash(attacker, NeedyLittleThings.raytraceEntities(target.world, attacker, target, getReach(attacker, stack)));
    }

    protected void applyEffects(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, int chi) {
        if (off) {
            if (isCharged(attacker, stack)) {
                NeedyLittleThings.knockBack(target, attacker, 1f);
                target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 20, 0));
            }
        } else {
            //main function. Check if previous move is also left click on the same target and trip if so
            if ((getLastMove(stack).isLeftClick() && getLastAttackedEntity(attacker.world, stack) == target) || isCharged(attacker, stack)) {
                //we're going on a trip on our favourite hooked... ship?
                TaoCasterData.getTaoCap(target).consumePosture(2f + target.getTotalArmorValue() / 5f, true);
            }
        }
        EnumHand other = off ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        if (TaoCombatUtils.getHandCoolDown(attacker, other) < 0.5f) {
            TaoCombatUtils.rechargeHand(attacker, other, 0.5f);
        }
    }

    @Override
    protected void perkDesc(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.DARK_RED + I18n.format("weapon.hands") + TextFormatting.RESET);
        tooltip.add(I18n.format("goulianqiang.trip"));
        tooltip.add(TextFormatting.ITALIC + I18n.format("goulianqiang.trip.riposte") + TextFormatting.RESET);
        tooltip.add(I18n.format("goulianqiang.bash"));
        tooltip.add(TextFormatting.ITALIC + I18n.format("goulianqiang.bash.riposte") + TextFormatting.RESET);
        tooltip.add(TextFormatting.BOLD + I18n.format("qiang.riposte") + TextFormatting.RESET);
    }
}
package com.jackiecrazi.taoism.common.item.weapon.melee.polearm.warhammer;

import com.jackiecrazi.taoism.api.NeedyLittleThings;
import com.jackiecrazi.taoism.api.PartDefinition;
import com.jackiecrazi.taoism.api.StaticRefs;
import com.jackiecrazi.taoism.capability.TaoCasterData;
import com.jackiecrazi.taoism.common.item.weapon.melee.TaoWeapon;
import com.jackiecrazi.taoism.potions.TaoPotion;
import com.jackiecrazi.taoism.utils.TaoCombatUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ChangChui extends TaoWeapon {
    /*
     * Just a long version of a chui. High range and power, medium defense, low speed and combo
     * Leap normal attacks deal double posture damage
     * Deals 2x downed damage in addition to the normal 2x, for a grand total of quadruple downed damage
     * Right click to sweep with radius of 4 and medium knock back, has a shortish cooldown
     * Parry special:
     * for the next 5 seconds, opponents will always take posture damage from you and cannot recover posture
     * Sweep will instantly use up this buff to majorly knock back all targets and remove half their current posture
     */
    public ChangChui() {
        super(0, 0.8f, 8.5f, 2f);
    }

    @Override
    protected double speed(ItemStack stack) {
        double ret = super.speed(stack) + 4d;
        if (getHand(stack) == EnumHand.OFF_HAND) {
            ret /= 1.5d;
        }
        ret -= 4d;
        return ret;
    }

    @Override
    public PartDefinition[] getPartNames(ItemStack is) {
        return StaticRefs.SIMPLE;
    }

    @Override
    public int getComboLength(EntityLivingBase wielder, ItemStack is) {
        return 1;
    }

    @Override
    public float critDamage(EntityLivingBase attacker, EntityLivingBase target, ItemStack item) {
        float ground = attacker.onGround ? 1f : 2f;
        float breach = TaoCasterData.getTaoCap(target).getDownTimer() > 0 ? 2f : 1f;
        return ground * breach;
    }

    @Override
    //default attack code to AoE
    protected void aoe(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, int chi) {
        if (attacker.onGround && getHand(stack)==EnumHand.OFF_HAND) {
            splash(attacker, target, 5);
        }
    }

    @Override
    protected void applyEffects(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker, int chi) {
        if (getHand(stack)==EnumHand.OFF_HAND) {
            float groundKB = attacker.onGround ? 1f : 1.3f;
            float chargeKB = isCharged(attacker, stack) ? 2f : 1f;
            NeedyLittleThings.knockBack(target, attacker, groundKB * chargeKB);
        } else {
            if (isCharged(attacker, stack)) {
                target.addPotionEffect(new PotionEffect(TaoPotion.ENFEEBLE, getChargeTimeLeft(attacker, stack)));
            }
        }
    }

    @Override
    protected void afterSwing(EntityLivingBase elb, ItemStack is) {
        if (getHand(is) == EnumHand.OFF_HAND) dischargeWeapon(elb, is);
        TaoCombatUtils.rechargeHand(elb, EnumHand.OFF_HAND, 0);
        TaoCombatUtils.rechargeHand(elb, EnumHand.MAIN_HAND, 0);
    }

    @Override
    public float getReach(EntityLivingBase p, ItemStack is) {
        return getHand(is) == EnumHand.OFF_HAND ? 5f : 6f;
    }

    @Override
    public int getMaxChargeTime() {
        return 100;
    }

    @Override
    public float postureMultiplierDefend(EntityLivingBase attacker, EntityLivingBase defender, ItemStack item, float amount) {
        return 0.5f;
    }

    public void attackStart(DamageSource ds, EntityLivingBase attacker, EntityLivingBase target, ItemStack item, float orig) {
        if (isCharged(attacker, item)) {
            if (getHand(item)==EnumHand.OFF_HAND)
                TaoCasterData.getTaoCap(target).consumePosture(TaoCasterData.getTaoCap(target).getMaxPosture() / 3f, true);
            else TaoCasterData.getTaoCap(target).consumePosture(postureDealtBase(attacker, target, item, orig), true);
        }
    }

    @Override
    protected void perkDesc(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.DARK_RED + I18n.format("weapon.hands") + TextFormatting.RESET);
        tooltip.add(TextFormatting.DARK_GREEN + I18n.format("weapon.disshield") + TextFormatting.RESET);
        tooltip.add(I18n.format("changchui.leap"));
        tooltip.add(I18n.format("changchui.downed"));
        tooltip.add(I18n.format("changchui.swipe"));
        tooltip.add(I18n.format("changchui.riposte"));
        tooltip.add(TextFormatting.ITALIC + I18n.format("changchui.swipe.riposte") + TextFormatting.RESET);
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
        return !attacker.onGround;
    }

    @Override
    public boolean isTwoHanded(ItemStack is) {
        return true;
    }
}

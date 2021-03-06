package com.jackiecrazi.taoism.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;

public interface ITaoStatCapability {
     float getQi();
     default int getQiFloored(){return (int)Math.floor(getQi());}
     void setQi(float amount);
     float addQi(float amount);
     boolean consumeQi(float amount);
     float getLing();
     void setLing(float amount);
     float addLing(float amount);
     boolean consumeLing(float amount);
     /**
      * this will return the cached cooldown for players, and will track mob swing instead. Because Mojang didn't implement attack timers and nobody else bothered.
      *
      * not cool, Mojang, not cool. If you want a combat update, start with attack windups and recoveries.
      */
     int getSwing();
     void setSwing(int amount);
     float getPosture();
     void setPosture(float amount);
     float addPosture(float amount);
     boolean consumePosture(float amount, boolean canStagger);
     int getParryCounter();
     void setParryCounter(int amount);
     void addParryCounter(int amount);
     int getRollCounter();
     void setRollCounter(int amount);
     void addRollCounter(int amount);
     Tuple<Float, Float> getPrevSizes();
     void setPrevSizes(float width, float height);
     int getComboSequence();
     void setComboSequence(int amount);
     void incrementComboSequence(int amount);
     void copy(ITaoStatCapability from);
     float getMaxLing();
     void setMaxLing(float amount);
     float getMaxPosture();
     void setMaxPosture(float amount);
     int getLingRechargeCD();
     void setLingRechargeCD(int amount);
     int getPostureRechargeCD();
     void setPostureRechargeCD(int amount);
     int getStaminaRechargeCD();
     void setStaminaRechargeCD(int amount);
     int getPosInvulTime();
     void setPosInvulTime(int time);
     long getLastUpdatedTime();
     void setLastUpdatedTime(long time);
     boolean isSwitchIn();
     void setSwitchIn(boolean suichi);
     int getOffhandCool();
     void setOffhandCool(int oc);
     ItemStack getOffHand();
     void setOffHand(ItemStack is);
     boolean isProtected();
     void setProtected (boolean protect);
     int getDownTimer();
     void setDownTimer(int time);
}

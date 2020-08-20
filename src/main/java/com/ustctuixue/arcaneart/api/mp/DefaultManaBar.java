package com.ustctuixue.arcaneart.api.mp;


import com.ustctuixue.arcaneart.api.APIConfig;
import lombok.Data;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;

@Data
public class DefaultManaBar implements IManaBar
{
    private double mana;

    private int regenCoolDown;

    @Override
    public double getMaxMana(LivingEntity parentLivingEntity)
    {
        return parentLivingEntity.getAttribute(CapabilityMP.MAX_MANA).getValue();
    }

    @Override
    public void setBaseMaxMana(double value, LivingEntity parentLivingEntity)
    {
        parentLivingEntity.getAttribute(CapabilityMP.MAX_MANA).setBaseValue(value);
    }

    private double magicExperience;

    private int magicLevel;

    /**
     * 消耗 MP 值
     *
     * @param manaIn 消耗的 MP 量
     */
    @Override
    public boolean consumeMana(double manaIn)
    {
        if (this.mana >= manaIn)
        {
            this.mana -= manaIn;
            return true;
        }
        return false;
    }

    @Override
    public boolean canTolerate(double complexity, LivingEntity parentEntity)
    {
        return parentEntity.getAttribute(CapabilityMP.CASTER_TIER).getValue() >= complexity;
    }

    /**
     * 增加魔法经验值，同时升级
     *
     * @param exp 经验值
     */
    @Override
    public void addMagicExperience(double exp, LivingEntity parentEntity)
    {
        this.setMagicExperience(exp + this.magicExperience, parentEntity);
    }

    /**
     * 每 tick 调用一次，用于回复冷却倒计时
     *
     * @return 是否可以开始回复
     */
    @Override
    public boolean coolDown()
    {

        if (regenCoolDown <= 0)
        {
            regenCoolDown = 0;
            return true;
        }
        regenCoolDown--;
        return false;
    }

    @Override
    public double getBaseComplexityTolerance(LivingEntity parentLivingEntity)
    {
        return parentLivingEntity.getAttribute(CapabilityMP.CASTER_TIER).getBaseValue();
    }

    @Override
    public void setBaseComplexityTolerance(double value, LivingEntity parentLivingEntity)
    {
        parentLivingEntity.getAttribute(CapabilityMP.CASTER_TIER).setBaseValue(value);
    }

    @Override
    public void setMagicExperience(double exp, LivingEntity parentEntity)
    {
        this.magicExperience = exp;
        if (exp < 0)                        // exp 是负数
        {
            if (magicLevel == 0)            // 不能再降级了
            {
                magicExperience = 0;
            }
            else
            {
                do                          // 循环降级，直到不能继续降级
                {
                    magicLevel--;
                    this.magicExperience += APIConfig.MP.Levelling.getExpToNextLvl(magicLevel);
                }while(this.magicExperience < 0 && magicLevel != 0);
                if (this.magicExperience < 0)       // 降到 0 了，但还是负数
                {
                    this.magicExperience = 0;
                }
            }
        }
        else{
            boolean levelUp = false;
            int newLevel = this.magicLevel;
            while (exp >= APIConfig.MP.Levelling.getExpToNextLvl(this.magicLevel))        // 该升级了
            {
                newLevel++;
                levelUp = true;
            }
            if (levelUp && MinecraftForge.EVENT_BUS.post(new MPEvent.LevelUp(parentEntity, this.magicLevel, newLevel)))
            {
                this.setBaseMaxMana(
                        APIConfig.MP.Levelling.getMaxMPForCurrentLevel(newLevel),
                        parentEntity
                );
                this.setBaseComplexityTolerance(
                        APIConfig.MP.Levelling.getComplexityTolerance(newLevel),
                        parentEntity
                );
                this.magicLevel = newLevel;

            }
        }
    }
}

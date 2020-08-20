package com.ustctuixue.arcaneart.spell;

import com.udojava.evalex.Expression;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.common.ForgeConfigSpec.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpellConfig
{
    private String path;

    public SpellConfig(String path)
    {
        this.path = path;
    }

    private DoubleValue baseManaCost = null;
    @Getter @Setter
    double defaultBaseManaCost = 0;

    private DoubleValue baseComplexity = null;
    @Getter @Setter
    double defaultBaseComplexity = 0;

    private ConfigValue<String> manaCostAmplifierExp;
    private Expression manaCostAmplifier = null;
    @Getter @Setter
    String defaultAmpExp;

    private ConfigValue<String> complexityAmplifierExp;
    private Expression complexityAmplifier = null;
    @Getter @Setter
    String defaultCmxExp;

    @Getter @Setter
    String[] variantComment;


    public double getBaseManaCost()
    {
        return baseManaCost.get();
    }

    public double getBaseComplexity()
    {
        return baseComplexity.get();
    }

    public Expression getManaCostAmplifier()
    {
        if (manaCostAmplifier == null)
        {
            manaCostAmplifier = new Expression(manaCostAmplifierExp.get());
        }
        return manaCostAmplifier;
    }

    public Expression getComplexityAmplifier()
    {
        if (complexityAmplifier == null)
        {
            complexityAmplifier = new Expression(complexityAmplifierExp.get());
        }
        return complexityAmplifier;
    }

    public final void load(Builder builder)
    {
        builder.push(path);

        baseManaCost = builder
                .comment("Basic mana cost of this spell")
                .defineInRange("baseManaCost", defaultBaseManaCost, 0, Double.MAX_VALUE);

        baseComplexity = builder
                .comment("Basic complexity")
                .defineInRange("baseComplexity", defaultBaseComplexity, -Double.MAX_VALUE, Double.MAX_VALUE);

        List<String> processedVariantComment = new ArrayList<>(Arrays.asList(variantComment));
        processedVariantComment.add("Expression Usage: https://github.com/uklimaschewski/EvalEx");

        List<String> manaAmpComment = new ArrayList<>(processedVariantComment);
        List<String> cmxAmpComment = new ArrayList<>(processedVariantComment);
        manaAmpComment.add(0, "Mana cost amplifier expression, the total mana cost will be cost base multiplied by this");
        cmxAmpComment.add(0, "Spell complexity amplifier expression, the total mana cost will be cost base multiplied by this");

        manaCostAmplifierExp = builder
                .comment(
                        manaAmpComment.toArray(new String[0])
                )
                .define("manaCostAmplifier", defaultAmpExp);

        complexityAmplifierExp = builder
                .comment(
                        cmxAmpComment.toArray(new String[0])
                )
                .define("complexityAmplifier", defaultCmxExp);


        loadExtraConfigValue(builder);
        builder.pop();
    }

    protected void loadExtraConfigValue(Builder builder)
    {

    }
}

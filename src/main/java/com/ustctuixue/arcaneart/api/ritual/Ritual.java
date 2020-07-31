package com.ustctuixue.arcaneart.api.ritual;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Supplier;

public class Ritual implements IForgeRegistryEntry<Ritual> {

    public static ForgeRegistry<Ritual> REGISTRY
            = (ForgeRegistry<Ritual>) new RegistryBuilder<Ritual>()
            .setType(Ritual.class)
            .setName(ArcaneArtAPI.getResourceLocation("ritual"))
            .create();

    private ResourceLocation name;
    private static final int MAX_DING_NUMBER = 9;

    public Ritual(IRitual iRitual, @Nonnull Item... ingredients) {
        this.setIngredients(ingredients).setExecRitual(iRitual);
    }

    @Getter
    private Item[] ingredients;

    @Getter @Setter
    private IRitual execRitual = null;

    @Getter @Setter
    private boolean rotatable = true;

    @Getter @Setter
    private boolean flippable = false;

    @Getter @Setter
    private double cost = 5000;

    @Getter @Setter
    private double consumeSpeed = 50;

    public Ritual setIngredients(@Nonnull Item... ingredients) {
        if(isValidIngredients(ingredients)) {
            this.ingredients = formatIngredients(ingredients);
            return this;
        }
        throw new IllegalArgumentException("Can not create ritual recipe with no item or more than 9 items.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ritual ritual = (Ritual) o;
        return matches(ritual.ingredients);
    }

    @Override
    public int hashCode() {
        int i = Arrays.hashCode(ingredients);
        return i;

    }

    private static final int[][] ROTATE_TRANS_MAT = {
            {6, 3, 0, 7, 4, 1, 8, 5, 2},//rotate 90
            {8, 7, 6, 5, 4, 3, 2, 1, 0},//rotate 180
            {2, 5, 8, 1, 4, 7, 0, 3, 6}//rotate 270
    };
    private static final int[][] FLIP_TRANS_MAT = {
            {2, 1, 0, 5, 4, 3, 8, 7, 6},//flip horizontal
            {6, 7, 8, 3, 4, 5, 0, 1, 2} //flip vertical
    };

    public boolean matches(Item[] Item) {

        Item[] ingredients = formatIngredients(Item);
        if(!isValidIngredients(Item)) {
            return false;
        }

        boolean result = true;
        if(this.rotatable && ingredientsTransMatch(ingredients, ROTATE_TRANS_MAT)) {
            return true;
        }
        if(this.flippable && ingredientsTransMatch(ingredients, FLIP_TRANS_MAT)) {
            return true;
        }
        return ingredientsTransMatch(ingredients, null);
    }

    private boolean ingredientsTransMatch(Item [] ingredients, int [][] transMat) {
        if(transMat == null) {
            for(int i = 0; i < MAX_DING_NUMBER; ++i) {
                if(!ItemMatches(this.ingredients[i], ingredients[i])){
                    return false;
                }
            }
            return true;
        }
        else {
            boolean result = true;
            for(int[] mat : transMat) {
                result = true;
                for(int i = 0; i < MAX_DING_NUMBER; ++i) {
                    result = ItemMatches(this.ingredients[mat[i]], ingredients[i]);
                    if(!result) break;
                }
                if(result) return true;
            }
            return false;
        }
    }

    static private boolean ItemMatches(Item itemRecipe, Item itemInput) {
        boolean recipeIsEmpty = (itemRecipe == null || itemRecipe == Items.AIR);
        boolean inputIsEmpty = (itemInput == null || itemInput == Items.AIR);
        if( recipeIsEmpty && inputIsEmpty ) {
            return true;
        }
        if( !recipeIsEmpty && !inputIsEmpty) {
            return itemRecipe.getItem() == itemInput.getItem();
        }
        return false;
    }

    static public boolean isValidIngredients(@Nonnull Item... ingredients) {
        if(ingredients.length <= 0 || ingredients.length > MAX_DING_NUMBER) {
            return false;
        }
        for(Item i : ingredients) {
            if( !(i == null || i == Items.AIR)) return true;
        }
        return false;
    }

    private static Item[] formatIngredients(@Nonnull Item... ingredients) {
        if(ingredients.length != MAX_DING_NUMBER) {
            Item[] Item = new Item[MAX_DING_NUMBER];
            System.arraycopy(ingredients, 0, Item, 0, ingredients.length);
            return Item;
        }
        return ingredients;
    }

    @Override
    public Ritual setRegistryName(ResourceLocation name) {
        this.name = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.name;
    }

    @Override
    public Class<Ritual> getRegistryType() {
        return Ritual.class;
    }

    public static class Builder {

        Ritual ritual;
        public Builder() {
            ritual = new Ritual(null, Items.BEDROCK);
        }
        public Builder setExecRitual(IRitual iRitual) {
            this.ritual.setExecRitual(iRitual);
            return this;
        }
        public Builder setIngredients(@Nonnull Item... item) {
            this.ritual.setIngredients(item);
            return this;
        }
        public Builder setRotatable(boolean rotatable) {
            this.ritual.setRotatable(rotatable);
            return this;
        }
        public Builder setFlippable(boolean flippable) {
            this.ritual.setFlippable(flippable);
            return this;
        }
        public Builder setManaCost(int cost) {
            this.ritual.setCost(cost);
            return this;
        }
        public Builder setManaConsumeSpeed(int speed) {
            this.ritual.setConsumeSpeed(speed);
            return this;
        }
        public Ritual create() {
            return this.ritual;
        }
        public Supplier<Ritual> supplier() {
            return this::create;
        }
    }
}

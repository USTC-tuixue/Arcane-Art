package com.ustctuixue.arcaneart.api.ritual;

import com.ustctuixue.arcaneart.api.ArcaneArtAPI;
import lombok.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Supplier;

@Builder @Data
public class Ritual implements IForgeRegistryEntry<Ritual>
{

    public static ForgeRegistry<Ritual> REGISTRY
            = (ForgeRegistry<Ritual>) new RegistryBuilder<Ritual>()
            .setType(Ritual.class)
            .setName(ArcaneArtAPI.getResourceLocation("ritual"))
            .create();

    private static final int MAX_DING_NUMBER = 9;

    private final Ingredient[] ingredients;

    private final Supplier<IRitualEffect> execRitual;

    private final boolean rotatable;

    private final boolean flippable;

    private final int cost;

    private final int consumeSpeed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ritual ritual = (Ritual) o;
        return matches(ritual.ingredients) || ritual.matches(this.ingredients);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(ingredients);
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

    public boolean matches(ItemStack[] itemStacks) {
        ItemStack[] input = new ItemStack[MAX_DING_NUMBER];
        int nullStacks = 0;
        System.arraycopy(itemStacks, 0, input, 0, Math.min(MAX_DING_NUMBER, itemStacks.length));
        for(int i = 0; i < MAX_DING_NUMBER; ++i) {
            if(input[i] == null) {
                input[i] = ItemStack.EMPTY;
                nullStacks++;
            }
        }
        if(nullStacks == MAX_DING_NUMBER) {
            return false;
        }
        if(this.rotatable && itemTransMatch(input, ROTATE_TRANS_MAT)) {
            return true;
        }
        if(this.flippable && itemTransMatch(input, FLIP_TRANS_MAT)) {
            return true;
        }
        return itemTransMatch(input, null);
    }

    private boolean itemTransMatch(ItemStack[] input, int[][] transMat) {
        if(transMat == null) {
            for(int i = 0; i < MAX_DING_NUMBER; ++i) {
                if(!ingredients[i].test(input[i])){
                    return false;
                }
            }
            return true;
        }
        else {
            boolean result;
            for(int[] mat : transMat) {
                result = true;
                for(int i = 0; i < MAX_DING_NUMBER; ++i) {
                    result = ingredients[i].test(input[i]);
                    if(!result) break;
                }
                if(result) return true;
            }
            return false;
        }
    }

    private boolean matches(Ingredient[] item) {

        Ingredient[] ingredients = formatIngredients(item);
        if(!isValidIngredients(ingredients)) {
            return false;
        }
        if(this.rotatable && ingredientsTransMatch(ingredients, ROTATE_TRANS_MAT)) {
            return true;
        }
        if(this.flippable && ingredientsTransMatch(ingredients, FLIP_TRANS_MAT)) {
            return true;
        }
        return ingredientsTransMatch(ingredients, null);
    }

    private boolean ingredientsTransMatch(Ingredient [] ingredients, int [][] transMat) {
        if(transMat == null) {
            for(int i = 0; i < MAX_DING_NUMBER; ++i) {
                if(!ItemMatches(this.ingredients[i], ingredients[i])){
                    return false;
                }
            }
            return true;
        }
        else {
            boolean result;
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

    static private boolean ItemMatches(Ingredient itemRecipe, Ingredient itemInput) {
        boolean recipeIsEmpty = (itemRecipe == null || itemRecipe.hasNoMatchingItems());
        boolean inputIsEmpty = (itemInput == null || itemInput.hasNoMatchingItems());
        if( recipeIsEmpty && inputIsEmpty ) {
            return true;
        }
        if( !recipeIsEmpty && !inputIsEmpty) {
            for(ItemStack i : itemRecipe.getMatchingStacks()) {
                if(!itemInput.test(i)) {
                    return false;
                }
            }
            for(ItemStack i : itemInput.getMatchingStacks()) {
                if(!itemRecipe.test(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isValidIngredients(@Nonnull Ingredient[] ingredients) {
        if(ingredients.length != MAX_DING_NUMBER) {
            return false;
        }
        for(Ingredient i : ingredients) {
            if( !(i == null || i.hasNoMatchingItems())) return true;
        }
        return false;
    }

    private static Ingredient[] formatIngredients(@Nonnull Ingredient... ingredients) {
        if(ingredients.length != MAX_DING_NUMBER) {
            Ingredient[] item = new Ingredient[MAX_DING_NUMBER];
            System.arraycopy(ingredients, 0, item, 0, Math.min(ingredients.length, MAX_DING_NUMBER));
            return item;
        }
        return ingredients;
    }


    ////////////////////////////////////////////////////
    // Forge Registry Entry part here
    ////////////////////////////////////////////////////

    @Getter @Nullable
    private ResourceLocation registryName;

    @Override
    public Ritual setRegistryName(ResourceLocation name) {
        this.registryName = name;
        return this;
    }

    @Override
    public Class<Ritual> getRegistryType() {
        return Ritual.class;
    }
}
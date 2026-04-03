package tfar.gearith.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import tfar.gearith.Constants;
import tfar.gearith.MItems;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        TextureMapping textureMapping = TextureMapping.particle(modLocation("item/thunder_club"));
        ResourceLocation resourceLocation = ModelTemplates.PARTICLE_ONLY.create(MItems.THUNDER_CLUB, textureMapping, itemModels.modelOutput);
        itemModels.itemModelOutput.accept(MItems.THUNDER_CLUB,ItemModelUtils.plainModel(resourceLocation));
    }
}

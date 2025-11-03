package com.sp.entity.client.renderer;

import com.sp.SPBRevamped;
import com.sp.entity.custom.BacteriaEntity;
import com.sp.entity.client.model.BacteriaModel;
import com.sp.init.ModModelLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/**
 * Minimal, safe renderer for BacteriaEntity. This intentionally does almost no rendering
 * but prevents the client from throwing a null renderer NPE when the entity is present.
 */
public class BacteriaRenderer extends MobEntityRenderer<BacteriaEntity, BacteriaModel<BacteriaEntity>> {
    private static final Identifier TEXTURE = new Identifier(SPBRevamped.MOD_ID, "textures/entity/bacteria/bacteria.png");

    public BacteriaRenderer(EntityRendererFactory.Context context) {
        super(context, new BacteriaModel<>(context.getPart(ModModelLayers.BACTERIA)), 0.5f);
    }

    @Override
    public Identifier getTexture(BacteriaEntity entity) {
        return TEXTURE;
    }
}

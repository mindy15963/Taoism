package com.jackiecrazi.taoism.client;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.jackiecrazi.taoism.Taoism;
import com.jackiecrazi.taoism.api.WeaponStatWrapper;
import com.jackiecrazi.taoism.client.stupidmodels.ModelWeapon;
import com.jackiecrazi.taoism.common.item.TaoItems;
import com.jackiecrazi.taoism.config.TaoConfigs;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Taoism.MODID)
public class ClientEvents {
	@SubscribeEvent
	public static void model(ModelRegistryEvent e) {
		ModelLoader.setCustomModelResourceLocation(TaoItems.weap, 0, new ModelResourceLocation(TaoItems.weap.getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(TaoItems.bow, 0, new ModelResourceLocation(TaoItems.bow.getRegistryName(), "inventory"));

		for (WeaponStatWrapper a : TaoConfigs.weapc.enabledParts.values()) {
			//int damage = TaoConfigs.weapc.lookupDamage(a.getName());

			/*for (MaterialStatWrapper m : MaterialsConfig.loggedMaterials.values()) {
				//System.out.println(new ModelResourceLocation(Taoism.MODID + ":parts/"+  m.name +"/" + a.getName().toLowerCase().replace(" ", "")).getResourcePath());
				ModelBakery.registerItemVariants(TaoItems.parts, new ModelResourceLocation(Taoism.MODID + ":parts/"+  m.name +"/" + a.getName().toLowerCase().replace(" ", "") , "inventory"));

			}*/
			//ModelLoader.setCustomModelResourceLocation(TaoItems.blueprint, damage, new ModelResourceLocation(TaoItems.blueprint.getRegistryName(), "inventory"));

			//ModelLoader.setCustomModelResourceLocation(TaoItems.blueprint, 0, new ModelResourceLocation(TaoItems.blueprint.getRegistryName(), "inventory"));
			//ModelLoader.setCustomModelResourceLocation(TaoItems.weap, 0, new ModelResourceLocation(TaoItems.weap.getRegistryName(), "inventory"));
			
			ModelBakery.registerItemVariants(TaoItems.weap, new ModelResourceLocation(Taoism.MODID + ":parts/"+ a.getName().toLowerCase().replace(" ", "")));
		}

	}

	@SubscribeEvent
	public static void modelDaWeaponz(ModelBakeEvent e) {
		//renderitem has an interesting method

		e.getModelRegistry().putObject(new ModelResourceLocation(Taoism.MODID + ":taoweapon", "inventory"), new ModelWeapon());
		Taoism.logger.debug("registered the weapon models");
	}
}

/*
0 = sabre blade
64 = sword blade
128 = macuahuitl blade
192 = broad sabre blade
256 = broadsword blade
320 = double headed hook blade
384 = flamberge blade
448 = single headed hook blade
512 = long sabre blade
576 = wide sabre blade
640 = long sword blade
704 = large sabre blade
768 = large sword blade
832 = sawtooth spear head
896 = spear head
960 = trident head
1024 = crown spear head
1 = handle
65 = convex handle
129 = shaft
193 = long shaft
2 = claws
66 = side blade
130 = deer horn blade
194 = skull guard
258 = wide guard
322 = normal guard
386 = tai chi guard
450 = large skull guard
514 = large guard
578 = flaming guard
642 = single sided axe head
706 = double sided axe head
770 = single bladed ge head
834 = double bladed ge head
898 = scythe head
962 = hammer head
1026 = single sided ji head
1090 = double sided ji head
3 = chain
67 = pommel
131 = butt spike
195 = crescent
 */

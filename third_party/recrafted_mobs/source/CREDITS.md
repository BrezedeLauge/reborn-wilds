# ReCrafted Mobs source snapshots

These targeted, unchanged source models and textures come from **ReCrafted Mobs 11.0**, by **GillsBills**, with help credited upstream to **Cave Fox, Kekkman, PinkuRinku, RavenVLemon, Ewanhowell5195, creepermax123 and MickeyJoeAlpha**. Individual-file artists are not separately specified by the pack.

Source: https://modrinth.com/resourcepack/recrafted-mobs
Author: https://www.planetminecraft.com/member/gillsbills/

The assets are licensed under **Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International**: https://creativecommons.org/licenses/by-nc-sa/4.0/ . The complete legal text is in `../LICENSE.txt`; the original license notice and project attribution are in the parent directory. The asset license is separate from the MIT license of original Dungeons Alive program code.

`IMPORT_MANIFEST.json` records every retained file hash. No resource-pack archive and no copied Minecraft game textures are included. Generated adaptations preserve this asset license and are identified in `design/cem-model-imports.json` and `design/cem-extra-model-imports.json`: Java rig conversion, explicit vanilla parent transforms, retained expressions and scale, lossless combined texture atlases, and the standalone Arch-Illager subtree/appropriate spawn-phase skin. These are community-authored source models, not a claim of extracted original-game meshes or keyframe clips.

The additional armor textures use their own material layer with the original animated bone transforms. Enchanted sheep retain the source `sheep_wool/sheep_wool.jem` as a separate outer layer with its original 64×64 UV layout, inflation and animation expression. The named cat and spotted-pig skins use an explicitly selected ordinary species `cat.jem`/`pig.jem` basis; the source's biome and weighted species choices are documented instead of being claimed as unique name rules. Two texture-only pets use the installed Goat and Turtle model factories with their matching authored texture. Native model geometry and Minecraft base textures are not copied into this source snapshot; factory references are recorded explicitly in the import manifest.

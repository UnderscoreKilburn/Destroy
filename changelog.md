------------------------------------------------------
Destroy 0.1.3-𝑖+2
------------------------------------------------------
### Changes
- General:
	- Improved general performance related to chemical reactions
- Vat tweaks:
	- Vats now have a heat capacity based on the blocks used to construct them, which is added to the contained Mixture's heat capacity when calculating changes in temperature
	- Heat capacity provided by Vat construction blocks is directly proportional to their thermal conductivity
	- In practice, this prevents extreme changes in temperature when heating or cooling down a Vat, and also allows Vats built out of highly conductive materials (i.e. copper) to gain heat faster (they previously did not)
	- Rewritten Vat fluid I/O to address many instances of matter loss or duplication when the volume of fluid inside a Vat changes while a reaction is taking place
	- Added a copper skin for Vat parts which is used when a Vat is built out of copper casings
- Bubble Cap tweaks:
	- Distillation columns now wait until the contents of their reboiler has stopped changing to perform the distillation process
	- Bubble Caps at the bottom of a distillation column can now auto-output distillation residue to a basin or drain below them via a basin-style spout
	- Bubble Caps in a distillation column now display their own temperature on their tooltip, with each Bubble Cap above the reboiler decreasing in temperature until the topmost Bubble Cap, which is at room temperature
	- Molecules condense in a Bubble Cap if their boiling point is higher than its temperature but lower than the temperature of the Bubble Cap below it, or if there is no Bubble Cap above it
- Centrifuge tweaks:
	- Mixture containing ions no longer yield invalid Mixtures when centrifuged
		- Note that Destroy currently assigns the same density to most ions so centrifuging is still generally not a good way to separate ions
	- Centrifuges will now only process even volumes of Mixture, trying to process an odd volume will leave 1 mB of Mixture in the input
	- Centrifuges will now avoid splitting a molecule if it can almost fit in one output in its entirety (this prevents micro impurities caused by floating point precision errors, e.g. when separating refinery gas)
- Drains now combine mixtures poured into them, similarly to fluid tanks
- Basins now accept mixtures that don't perfectly match their current contents, as long as they contain the same molecules (this makes mercury recycling in brine processing accessible without copious amounts of redstone logic)
- Threshold switches now have a configurable threshold in millibuckets when pointed at Bubble Caps or Vats
- Tapping recipes are now data-driven and can be modified by datapacks

### Fixes
- Fixed several recipes yielding mixtures with impossible concentrations

------------------------------------------------------
Destroy 0.1.3-𝑖+1
------------------------------------------------------
Requires **Create 6.0.8** and **Petrolpark's Library 1.4.27**.

### Additions
- Fluid Vessels from Create: Connected can now hold Mixtures

### Fixes
- Fixed compatibility issue with Steam 'n' Rails 1.7.2+
- Fixed JEI freezing for a few seconds when opened for the first time on a dedicated server
- Fixed molten glass not being rendered properly in first person when glassblowing
- Fixed pumpjacks not being rendered properly when placed far away from the world origin
- Fixed pumpjacks outputting "flowing" crude oil instead of the correct fluid (this broke a few recipes, notably ANFO)
------------------------------------------------------
Destroy 0.1.3-𝑖+0
------------------------------------------------------
Requires **Create 6.0.8** and **Petrolpark's Library 1.4.23**.

### Changes
- Updated models and textures for the Vat, Bubble Cap, Centrifuge, Pumpjack, Blacklight and Creative Pump
- Multiple mixtures can now travel down the same pipe network and will visually mix when seen through transparent pipes
- Faucets from Supplementaries now properly transfer mixtures and no longer bypass pollution when voiding them using a sponge
- Vat sensors now use the same units as the ones displayed in tooltips when configuring their lower and upper bound (i.e. °C/°F and kPa instead of K and Pa)
- Voiding mixtures through an open pipe is no longer restricted to 1 mB/tick
- Molecules are now displayed as their 3D model in JEI by default
- Removed the manual crafting recipe for circuit boards
- Removed the steel tag from stainless steel items
- Water no longer contributes to greenhouse gas pollution

### Fixes
- Fixed some blocks not animating correctly
- Fixed fireproofing recipes duplicating items when applied to more than one item at a time
- Fixed a crash related to viewing a fireproofing recipe in JEI while on a dedicated server
- Fixed a crash when trying to process any block with the Extrusion Die
- Fixed right click interactions with some of Create's configurable blocks (notably copper valves and brass redstone components)
- Fixed mixed explosives and periodic table blocks not working properly on dedicated servers
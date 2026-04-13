------------------------------------------------------
Destroy 0.1.3-𝑖+1
------------------------------------------------------
Requires **Create 6.0.8** and **Petrolpark's Library 1.4.27**.

# Additions
- Fluid Vessels from Create: Connected can now hold Mixtures

# Fixes
- Fixed compatibility issue with Steam 'n' Rails 1.7.2+
- Fixed JEI freezing for a few seconds when opened for the first time on a dedicated server
- Fixed molten glass not being rendered properly in first person when glassblowing
- Fixed pumpjacks not being rendered properly when placed far away from the world origin
- Fixed pumpjacks outputting "flowing" crude oil instead of the correct fluid (this broke a few recipes, notably ANFO)
------------------------------------------------------
Destroy 0.1.3-𝑖+0
------------------------------------------------------
Requires **Create 6.0.8** and **Petrolpark's Library 1.4.23**.

# Changes
- Updated models and textures for the Vat, Bubble Cap, Centrifuge, Pumpjack, Blacklight and Creative Pump
- Multiple mixtures can now travel down the same pipe network and will visually mix when seen through transparent pipes
- Faucets from Supplementaries now properly transfer mixtures and no longer bypass pollution when voiding them using a sponge
- Vat sensors now use the same units as the ones displayed in tooltips when configuring their lower and upper bound (i.e. °C/°F and kPa instead of K and Pa)
- Voiding mixtures through an open pipe is no longer restricted to 1 mB/tick
- Molecules are now displayed as their 3D model in JEI by default
- Removed the manual crafting recipe for circuit boards
- Removed the steel tag from stainless steel items
- Water no longer contributes to greenhouse gas pollution

# Fixes
- Fixed some blocks not animating correctly
- Fixed fireproofing recipes duplicating items when applied to more than one item at a time
- Fixed a crash related to viewing a fireproofing recipe in JEI while on a dedicated server
- Fixed a crash when trying to process any block with the Extrusion Die
- Fixed right click interactions with some of Create's configurable blocks (notably copper valves and brass redstone components)
- Fixed mixed explosives and periodic table blocks not working properly on dedicated servers
# PixelWorld
Game created using LibGDX and our minds.
# Creating / Modifying maps
To load a .tmx map in the game (created using Tiled) you must set this properties:
PlayerPos - Starting pos of the player (Formatted as X:Y)
Story - Optional story included in map.
You must also create this layers (unordered):
### Background [Tile]
Background layer is used to draw each tile that must be behind entities (like floors etc)
### Top [Tile]
Top layer is used to draw everything on top of entities (trees etc)
### Collisions [Object]
Collisions layer contains rectangles that entities cannot overlap (used to define walls / closed areas)
### Spawn [Object]
Contains every spawn points for enemies. You must set :
Type - Name of the monster to spawn,
Number - Number of monsters to spawn.
### Interactions [Object]
Contains elements that should interact with player, Type defines type of element. Currently there are:
savePillars - Saving pillar
### NPC [Object]
Contains NPCs for the story / interaction with player. You must set:
Name - name of the NPC (needed for story)
Texture - name of the subdirectory of /core/assets/characters/npc/ containing texture for the npc
Actions - name of files contained inside that folder (for every action)
# Creating .story files
Story files contain an action in every line. You must specify 3 elements, separated by TAB operator (\t) :
Name - Name of the NPC executing the action. If you want to use the camera you must use "Camera" as name
Action - Action to perform
Parameter - Parameter for the action
If you are moving an NPC you can:
Talk - The NPC starts a dialog. Parameter is the content of dialog.
Move - The NPC moves around the map. Parameter is target, expressed as [TargetX]:[TargetY]
If you are moving the Camera you can:
Idle - Control passes to player. Parameter is ignored
Set - Camera points to target. Parameter expressed as [TargetX]:[TargetY]
Move - Camera moves at set speed to target. Parameter like set.
Spd - Changes speed of the camera movement. Parameter is new speed.

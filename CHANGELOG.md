1.0.3
-----

### Fixes
* Fixed client crashes caused by the skin changes of other unloaded players who are not loaded in the client's world because they are too far away.

1.0.2
-----

### Additions
* Added log messages for when a player changes his skin, both on the client and on the server. This should help tracking down those nasty bugs where someone's game crashes just because of a skin change.

### Removals
* Removed the world-specific skin folders. First, that feature introduced crashes on clients in cases where there integrated server hasn't been initialized yet, i.e. the user directly went to multiplayer without opening a singleplayer world first. Second, it was only rarely used. In an ideal world, all data related to a world would be stored in that world. But most mods don't allow for such a thing anyway, so you store all your extra-world-data in another folder elsewhere and then copy or link the required files over when you need to. And, on another note ... who thought about what happens if a skin is required for two separate worlds?

1.0.1
-----

### Fixes
* Replace the snapshot dependency on GuiLib with a proper release.

1.0.0
-----

### Notes
* This is the first iteration of the mod, so there aren't any changes to mention here.

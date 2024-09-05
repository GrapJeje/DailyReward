# Daily Reward, Minecraft Version 1.15.2
This is a custom made plugin for @Camonetwork.

## Dependency's
PlayerPoints: https://www.spigotmc.org/resources/playerpoints.80745/

CratesPlus: https://www.spigotmc.org/resources/cratesplus-free-crates-plugin-1-8-1-16-4.5018/

## Soft Dependency's

Realtime: Custom plugin @CamoNetwork for the minetopia

## Permissions

camo.dailyrewards.open : default Yes - Permission to open the main menu.

camo.admin.reload : default OP - Permission to reload the plugin.

## Commands

/Daily : Permission = camo.dailyrewards.open - Opens the main menu of the Daily Reward system.

/Daily info : Permission = Default - Opens an info menu about the plugin.

/Daily Reload : Permission = camo.admin.reload - Reload the config of the plugin.

## Config's

### Config.yml
Enabled : boolean || Set if the /Daily command is enabled.

ResetTime : Calendar || Set the reset time of the Daily Rewards **CAN ONLY BE RELOADED WITH A PLUGIN RELOAD!**

Players : List String || Keys are the UUID of the players.

Player.Dag : int || The day the player is currently.

Player.Streak : int || The longest streak from the player.

Player.Serverday : int || The day that can be claimed.

Player.Claimed : boolean || Check if the item already has been claimed.

Player.Bought : boolean || Check if the item already has been bought.

Streak : List String || Keys are the UUID of the player with the all time longest streak.

### Items.yml
Items : List String || The keys of the days.

Rewards : List String || The rewards you can get. 

    Money_amount || How many cash you get (In 5k).

    Candy_amount || How many candy voucher you can get.

    Key_Keyname || Wich key you can get.

    Material.MATERIAL.AMOUNT || How many of wich material you can get.

Lores : List String || The lore beneath the item in the menu.

Key : boolean || If the item is a key, set to true.

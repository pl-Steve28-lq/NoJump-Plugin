package com.steve28.plugin

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin

class Main: JavaPlugin() {
    val isJumpable: HashMap<String, Boolean> = HashMap()

    override fun onEnable() {
        logger.info("Plugin enabled")
        this.server.pluginManager.registerEvents(Event(), this)
        for (player in Bukkit.getOnlinePlayers()) {
             isJumpable[player.name] = false
        }
        getCommand("NoJump")?.setExecutor(Command())
    }

    override fun onDisable(){
        logger.info("Plugin disabled")
    }

    inner class Event: Listener {
        @EventHandler
        fun onPlayerMove(event: PlayerMoveEvent) {
            val player = event.player
            //Bukkit.broadcastMessage("${player.name} Moved!")
            val isJumping = player.velocity.y
            if (isJumping > -0.0784000015258789 && isJumpable[player.name]!!) {
                player.health = 0.0
            }
        }
    }

    inner class Command: CommandExecutor {
        override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<out String>): Boolean {
            val sender = sender as Player
            for (player in Bukkit.getOnlinePlayers()) {
                if (isJumpable[player.name] == null) isJumpable[player.name] = false
            }
            when (args.size) {
                0 -> {
                    isJumpable[sender.name] = !isJumpable[sender.name]!!
                    Broadcast(sender.name)
                }
                1 -> {
                    when (args[0]) {
                        "true" -> isJumpable[sender.name] = true
                        "false" -> isJumpable[sender.name] = false
                        else -> isJumpable[args[0]] = !isJumpable[args[0]]!!
                    }
                    Broadcast(sender.name)
                }
                2 -> {
                    when (args[1]) {
                        "true" -> isJumpable[args[0]] = true
                        "false" -> isJumpable[args[0]] = false
                    }
                    Broadcast(args[0])
                }
            }
            return false
        }
    }

    fun Broadcast(name: String) {
        Bukkit.broadcastMessage("${ChatColor.AQUA}${name}${ChatColor.RESET}님의 점프 가능 여부는 이제 ${ChatColor.RED}${!isJumpable[name]!!}${ChatColor.RESET} 입니다.")
    }
}
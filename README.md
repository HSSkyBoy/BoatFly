# [English introduction](README-EN.md)

此Fabric模组项目是**基于** [EricGilerson 的 BoatFly](https://github.com/EricGilerson/BoatFly) 进行了**代码重构**而非单纯构建，致力于保持在 Minecraft 1.21 及以上版本的可用性。

-----
# Boat Fly (船只飞行)



一个简单的客户端 Fabric 模组，让你能驾驶船只在空中自由飞行，并完全自定义其速度。

此模组可能被视为对游戏机制的修改，在**非无作弊 (Non-Anarchy)** 伺服器（例如：[Hypixel](us.hypixlel.net)）上使用可能会导致**账户封禁**。请务必在了解风险的前提下使用。


### 🚤 功能特性

   * **飞行开关**: 透过一个按键轻松切换普通船只模式与飞行模式。

   * **速度控制**: 在飞行过程中随时调整船只的飞行速度。

   * **垂直升降**: 使用跳跃键来控制上升。放开后，船只会受重力影响下降。

   * **纯客户端**: 本模组完全运行于客户端，伺服器无需安装。（实际效果因伺服器的反作弊插件而异）。


### ⚙️ 操作与设定

所有按键都可以在 Minecraft 的「按键绑定」中更改。
![keystroke](https://cdn.modrinth.com/data/cached_images/17d5edfa61c9327fbbe3bb506d2477ef1cac586e_0.webp)

    **切换船只飞行*\: `B` (预设)

    **增加速度*\: `I` (预设)

    **降低速度*\: `O` (预设)

    **升空*\: `空白键` (跳跃键)


#### 指令 (实验性)
![Commad](https://cdn.modrinth.com/data/cached_images/ca76eabaae847d94fa334b191766db5cd6c40ff9_0.webp)

* `/boatspeed <数值>`

* 设定船只的飞行速度。

* 例如: `/boatspeed 18.5` 会将速度设定为 18.5 格/秒。

#### 更改可视化

当你启用飞行或调整速度时，聊天栏会显示相应的提示讯息。
![Text](https://cdn.modrinth.com/data/cached_images/5aea15feb44b849b20f60eec5373b07cf07bbc61_0.webp)

### 🛠️ 安装说明


1.  确保您已经安装了 [Fabric Loader](https://fabricmc.net/use/installer/)。

2.  下载 [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)。

3.  下载本模组的最新版本。

4.  将 **Fabric API 的 `.jar` 档案与本模组的 `.jar` 档案** 一起放入您的 `.minecraft/mods` 资料夹中。

5.  启动游戏！

6. 进入世界并坐到船上。

-----
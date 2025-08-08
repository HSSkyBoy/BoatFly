package com.boatfly;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoatFlyClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("BoatFly");
    public static final String MOD_ID = "boatfly";

    private static KeyBinding boatFlightKey;
    private static KeyBinding increaseSpeedKey;
    private static KeyBinding decreaseSpeedKey;

    private static boolean isBoatFlyEnabled = false;
    private static double boatSpeedMultiplier = 1.0;
    private static double currentBoatVelocity = 8.0;

    private static final double DEFAULT_SPEED = 8.0;
    private static final double SPEED_INCREMENT = 1.0;
    private static final double SPEED_DECREMENT = -1.0;
    private static final double JUMP_VELOCITY = 0.3;
    private static final double MIN_SPEED = 0.0;

    @Override
    public void onInitializeClient() {
        boatFlightKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MOD_ID + ".toggle_fly", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "category." + MOD_ID + ".main"));
        increaseSpeedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MOD_ID + ".increase_speed", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category." + MOD_ID + ".main"));
        decreaseSpeedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + MOD_ID + ".decrease_speed", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "category." + MOD_ID + ".main"));

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommandManager.literal("boatspeed")
                .then(ClientCommandManager.argument("speed", FloatArgumentType.floatArg((float) MIN_SPEED))
                    .executes(context -> {
                        float speedValue = FloatArgumentType.getFloat(context, "speed");
                        changeSpeed(speedValue);
                        context.getSource().sendFeedback(Text.translatable("message." + MOD_ID + ".speed_changed", String.format("%.2f", currentBoatVelocity)));
                        return 1;
                    }));
            dispatcher.register(command);
        });
    }

    /**
     * 在每個客戶端 tick 結束時調用此方法，處理所有邏輯。
     * @param client MinecraftClient 實例
     */
    private void onClientTick(MinecraftClient client) {

        if (boatFlightKey.wasPressed()) {
            isBoatFlyEnabled = !isBoatFlyEnabled;
            if (isBoatFlyEnabled) {
                changeSpeed(DEFAULT_SPEED);
                if (client.player != null) {
                    client.player.sendMessage(Text.translatable("message." + MOD_ID + ".fly_enabled", String.format("%.2f", currentBoatVelocity)), false);
                }
            } else {
                if (client.player != null) {
                    client.player.sendMessage(Text.translatable("message." + MOD_ID + ".fly_disabled"), false);
                }
            }
        }

        if (!isBoatFlyEnabled) {
            return;
        }

        if (increaseSpeedKey.wasPressed()) {
            changeSpeed(currentBoatVelocity + SPEED_INCREMENT);
            if (client.player != null) {
                client.player.sendMessage(Text.translatable("message." + MOD_ID + ".speed_changed", String.format("%.2f", currentBoatVelocity)), false);
            }
        }

        if (decreaseSpeedKey.wasPressed()) {
            if (currentBoatVelocity > MIN_SPEED) {
                changeSpeed(currentBoatVelocity + SPEED_DECREMENT);
                if (client.player != null) {
                    client.player.sendMessage(Text.translatable("message." + MOD_ID + ".speed_changed", String.format("%.2f", currentBoatVelocity)), false);
                }
            }
        }
        
        if (client.player == null || !client.player.hasVehicle()) {
            return;
        }
        Entity vehicle = client.player.getVehicle();

        if (client.options.jumpKey.isPressed()) {
            Vec3d velocity = vehicle.getVelocity();
            vehicle.setVelocity(velocity.x, JUMP_VELOCITY, velocity.z);
        }

        if (client.options.forwardKey.isPressed() && boatSpeedMultiplier != 1.0) {
            Vec3d velocity = vehicle.getVelocity();
            Vec3d newVelocity = new Vec3d(velocity.x * boatSpeedMultiplier, velocity.y, velocity.z * boatSpeedMultiplier);
            vehicle.setVelocity(newVelocity);
        }
    }

    /**
     * 改變船的速度設定值，並計算相應的速度乘數。
     * @param newSpeed 新的目標速度（單位：blocks/s）
     */
    private void changeSpeed(double newSpeed) {
        currentBoatVelocity = Math.max(MIN_SPEED, newSpeed);
        boatSpeedMultiplier = calculateMultiplier(currentBoatVelocity);
        
        LOGGER.info("Boat speed set to {} b/s, multiplier is {}.", String.format("%.2f", currentBoatVelocity), String.format("%.5f", boatSpeedMultiplier));
    }

    /**
     * 根據目標速度計算一個內部乘數。
     * @param velocity 目標速度
     * @return 計算出的速度乘數
     */
    private double calculateMultiplier(double velocity) {
        // f(v) = (-5.33893 * (ln(v - 8 + 11.9072))^(-3.31832) + 1.26253)^(0.470998)
        if (velocity <= 0) return 0;
        
        double logInput = velocity - 8.0 + 11.9072;
        if (logInput <= 0) return 1.0;
        
        double term1 = -5.33893 * Math.pow(Math.log(logInput), -3.31832);
        double base = term1 + 1.26253;
        if (base < 0) return 1.0;

        return Math.pow(base, 0.470998);
    }
}

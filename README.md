# DollyMod

A Minecraft Forge 1.20.1 mod that adds **Dolly** — a tameable companion that behaves like a vanilla wolf (cooked chicken to tame, sit/follow/teleport, combat) with her own spawn egg, Beagle model, and howl.

See [CHANGELOG.md](CHANGELOG.md) for release history.

## Requirements

- JDK **17** (Minecraft 1.20.1 / Forge 47.4.0)
- Internet on first build (Gradle downloads Forge + Minecraft)

## Run in development

```bash
export JAVA_HOME=/path/to/jdk-17   # if needed
./gradlew runClient
```

In-game: Creative → Spawn Eggs → **Dolly Spawn Egg**, or `/summon dollymod:dolly`. Right-click with **cooked chicken** to tame (bones do nothing). Dolly is immortal to normal damage.

## Build a jar

```bash
./gradlew build
```

Output: `build/libs/dollymod-1.1.1.jar`

## Project layout (learning map)

| Path | Role |
|------|------|
| `DollyMod.java` | Mod entry (`@Mod`) — registers deferred content |
| `entity/DollyEntity.java` | Extends `Wolf`; custom sound hooks |
| `init/ModEntities.java` | EntityType `dollymod:dolly` |
| `init/ModItems.java` | Spawn egg |
| `init/ModSounds.java` | SoundEvent ids |
| `init/ModEvents.java` | Attributes + creative tab |
| `client/model/DollyModel.java` | Blockbench Beagle mesh + wolf-style sit/walk anims |
| `client/DollyRenderer.java` | Draws Dolly with DollyModel |
| `client/ClientModEvents.java` | Registers model layer + renderer (client only) |
| `assets/dollymod/textures/entity/dolly.png` | Beagle texture (64×32) |
| `assets/dollymod/sounds/dolly_howl.ogg` | Beagle howl (angry/growl sound) |
| `assets/dollymod/sounds.json` | Maps sound events → audio |

## Asset checklist

### Texture (needed for a real look)
Place a PNG at:

`src/main/resources/assets/dollymod/textures/entity/dolly.png`

While we use the vanilla wolf model, this file should follow the **wolf UV layout** (same as `textures/entity/wolf/wolf.png`). A beige placeholder ships in-repo so she is not purple/black; replace it with Dolly’s skin when ready.

### Custom Blockbench model (later)
1. Blockbench → Java Entity / Modded Entity (1.20+)
2. Export Java class → `client/model/DollyModel.java` (fix package/name)
3. Export texture → replace `dolly.png`
4. Register layer definition in `ClientModEvents`
5. Point `DollyRenderer` at `DollyModel` instead of `WolfModel`

### Custom sounds (later)
Drop `.ogg` files under `assets/dollymod/sounds/` and update `sounds.json` to reference them instead of `minecraft:mob/wolf/...`.

## Versions

- Minecraft `1.20.1`
- Forge `47.4.0`
- Mod id `dollymod`
- Package `com.samhutchinson.dollymod`

# BedrockAdapter

**BedrockAdapter** is a Paper plugin for detecting Bedrock players connected via Geyser/Floodgate and exposing an effective UUID for use with rank systems, MySQL databases, and payment integrations like Stripe.

## Features

- **Bedrock Player Detection**: Automatically detects players connecting from Bedrock Edition via Geyser/Floodgate
- **UUID Caching**: In-memory cache for effective UUID management
- **Floodgate Integration**: Seamless integration with Floodgate API
- **Payment Compatibility**: Tools to handle Bedrock players differently in payment systems
- **Paper 1.21 Support**: Built for the latest Paper server software

## Installation

1. **Renommer le package** (optionnel) : le code utilise `tonpackage.bedrock`
   comme placeholder. Renomme-le dans tous les fichiers `.java`, le `pom.xml`
   et `plugin.yml` (champ `main`) si tu veux ton propre namespace.

2. **Compiler** :
   ```
   mvn clean package
   ```
   Le jar sera genere dans `target/BedrockAdapter.jar`.

3. **Placer les jars sur CHAQUE serveur Paper** (pas seulement le proxy) :
   - `Floodgate-Spigot.jar` (a telecharger sur https://geysermc.org/download -> Floodgate -> Spigot/Paper)
   - `BedrockAdapter.jar` (ce plugin)

4. **Cote proxy BungeeCord**, installer :
   - `Geyser-BungeeCord.jar`
   - `Floodgate-BungeeCord.jar`

5. **Copier le fichier `key.pem`** genere par Floodgate sur le proxy vers le
   dossier `plugins/floodgate/` de chaque serveur Paper (doit etre identique
   partout, sinon l'auth echoue).

## Colonne SQL a ajouter

```sql
ALTER TABLE joueurs ADD COLUMN is_bedrock BOOLEAN DEFAULT FALSE;
```

## Utilisation dans ton systeme de grades existant

Remplace tes appels `player.getUniqueId()` utilises pour les lookups MySQL par :

```java
UUID uuidPourMySQL = PlayerUUIDCache.getEffectiveUUID(player.getUniqueId());
```

Et pour bloquer/adapter Stripe selon la plateforme :

```java
BedrockPlayerManager manager = bedrockAdapterPlugin.getBedrockManager();
if (!manager.isEligibleForPayment(player)) {
    // rediriger vers une autre methode de paiement ou informer le joueur
}
```

## Fichiers inclus

- `pom.xml` — build Maven (Paper 1.21 + Floodgate API)
- `plugin.yml` — depend de `Floodgate`
- `BedrockAdapterPlugin.java` — classe principale
- `BedrockPlayerManager.java` — logique de detection/UUID
- `PlayerUUIDCache.java` — cache en memoire
- `BedrockJoinListener.java` — listener join/quit

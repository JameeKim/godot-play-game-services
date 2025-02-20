package com.jacobibanez.plugin.android.godotplaygameservices

import android.util.Log
import com.google.android.gms.games.PlayGamesSdk
import com.jacobibanez.plugin.android.godotplaygameservices.achievements.AchievementsProxy
import com.jacobibanez.plugin.android.godotplaygameservices.friends.FriendsProxy
import com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.LeaderboardsProxy
import com.jacobibanez.plugin.android.godotplaygameservices.signals.getSignals
import com.jacobibanez.plugin.android.godotplaygameservices.signin.SignInProxy
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

/**
 * This is the main Godot Plugin class exposing the interfaces to use with Godot. In this class you
 * will find all the methods that can be called in your game via GDScript.
 */
class GodotAndroidPlugin(godot: Godot) : GodotPlugin(godot) {

    /** @suppress */
    override fun getPluginName() = BuildConfig.GODOT_PLUGIN_NAME

    private val signInProxy = SignInProxy(godot)
    private val achievementsProxy = AchievementsProxy(godot)
    private val leaderboardsProxy = LeaderboardsProxy(godot)
    private val friendsProxy = FriendsProxy(godot)

    /** @suppress */
    override fun getPluginSignals(): MutableSet<SignalInfo> {
        return getSignals()
    }

    /**
     * This method initializes the Play Games SDK. It should be called right after checking that
     * the plugin is loaded into Godot, for example:
     *
     * ```
     * func _ready() -> void:
     * 	if Engine.has_singleton(_plugin_name):
     * 		print("Plugin found!")
     * 		var android_plugin := Engine.get_singleton(_plugin_name)
     * 		android_plugin.initialize()
     * 	else:
     * 		printerr("No plugin found!")
     *```
     *
     * If the user has automatic sign-in enabled, the initialization will check for authentication.
     */
    @UsedByGodot
    fun initialize() {
        Log.d(pluginName, "Initializing Google Play Game Services")
        PlayGamesSdk.initialize(activity!!)
    }

    /**
     * Use this method to check if the user is already authenticated. If the user is authenticated,
     * a popup will be shown on screen.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.SignInSignals.userAuthenticated] signal.
     */
    @UsedByGodot
    fun isAuthenticated() = signInProxy.isAuthenticated()

    /**
     * Use this method to provide a manual way to the user for signing in.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.SignInSignals.userSignedIn] signal.
     */
    @UsedByGodot
    fun signIn() = signInProxy.signIn()

    /**
     * Use this method to increment a given achievement in the given amount. For normal achievements,
     * use the [unlockAchievement] method instead.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.AchievementsSignals.achievementUnlocked] signal.
     *
     * @param achievementId The achievement id.
     * @param amount The number of steps to increment by. Must be greater than 0.
     */
    @UsedByGodot
    fun incrementAchievement(achievementId: String, amount: Int) =
        achievementsProxy.incrementAchievement(achievementId, amount)

    /**
     * Call this method and subscribe to the emitted signal to receive the list of the game
     * achievements in JSON format. The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.AchievementsSignals.achievementsLoaded]
     * signal contains a representation of the [com.google.android.gms.games.achievement.Achievement](https://developers.google.com/android/reference/com/google/android/gms/games/achievement/Achievement) class.
     *
     * @param forceReload If true, this call will clear any locally cached data and attempt to fetch
     * the latest data from the server.
     */
    @UsedByGodot
    fun loadAchievements(forceReload: Boolean) =
        achievementsProxy.loadAchievements(forceReload)

    /**
     * Use this method to reveal a hidden achievement to the current signed player. If the achievement
     * is already unlocked, this method will have no effect.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.AchievementsSignals.achievementRevealed] signal.
     *
     * @param achievementId The achievement id.
     */
    @UsedByGodot
    fun revealAchievement(achievementId: String) =
        achievementsProxy.revealAchievement(achievementId)

    /**
     * Use this method to open a new window with the achievements of the game, and the progress of
     * the player to unlock those achievements.
     */
    @UsedByGodot
    fun showAchievements() = achievementsProxy.showAchievements()

    /**
     * Immediately unlocks the given achievement for the signed in player. If the achievement is
     * secret, it will be revealed to the player.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.AchievementsSignals.achievementUnlocked] signal.
     *
     * @param achievementId The achievement id.
     */
    @UsedByGodot
    fun unlockAchievement(achievementId: String) =
        achievementsProxy.unlockAchievement(achievementId)

    /**
     * Shows a new screen with all the leaderboards for the game.
     */
    @UsedByGodot
    fun showAllLeaderboards() = leaderboardsProxy.showAllLeaderboards()

    /**
     * Shows a new screen for a specific leaderboard.
     *
     * @param leaderboardId The leaderboard id.
     */
    @UsedByGodot
    fun showLeaderboard(leaderboardId: String) =
        leaderboardsProxy.showLeaderboard(leaderboardId)

    /**
     * Shows a specific leaderboard for a given span of time.
     *
     * @param leaderboardId The leaderboard id.
     * @param timeSpan The time span for the leaderboard refresh. It can be any of the [com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.TimeSpan] values.
     */
    @UsedByGodot
    fun showLeaderboardForTimeSpan(leaderboardId: String, timeSpan: Int) =
        leaderboardsProxy.showLeaderboardForTimeSpan(leaderboardId, timeSpan)

    /**
     * Shows a specific leaderboard for a given span of time and a specific type of collection.
     *
     * @param leaderboardId The leaderboard id.
     * @param timeSpan The time span for the leaderboard refresh. It can be any of the [com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.TimeSpan] values.
     * @param collection The collection type for the leaderboard. It can be any of the [[com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.Collection]] values.
     */
    @UsedByGodot
    fun showLeaderboardForTimeSpanAndCollection(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) = leaderboardsProxy.showLeaderboardForTimeSpanAndCollection(
        leaderboardId, timeSpan, collection
    )

    /**
     * Submits the score to the leaderboard for the currently signed-in player. The score is ignored
     * if it is worse (as defined by the leaderboard configuration) than a previously submitted
     * score for the same player.
     *
     * @param leaderboardId The leaderboard id.
     * @param score The raw score value. For more details, please [see this page](https://developers.google.com/games/services/common/concepts/leaderboards).
     */
    @UsedByGodot
    fun submitScore(leaderboardId: String, score: Int) =
        leaderboardsProxy.submitScore(leaderboardId, score)

    /**
     * Call this method and subscribe to the emitted signal to receive the score of the currently
     * signed in player for the given leaderboard, time span, and collection in JSON format.
     * The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.scoreLoaded]
     * signal contains a representation of the [com.google.android.gms.games.leaderboard.LeaderboardScore](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/LeaderboardScore) class.
     *
     * @param leaderboardId The leaderboard id.
     * @param timeSpan The time span for the leaderboard refresh. It can be any of the [com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.TimeSpan] values.
     * @param collection The collection type for the leaderboard. It can be any of the [[com.jacobibanez.plugin.android.godotplaygameservices.leaderboards.Collection]] values.
     */
    @UsedByGodot
    fun loadPlayerScore(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) = leaderboardsProxy.loadPlayerScore(leaderboardId, timeSpan, collection)

    /**
     * Call this method and subscribe to the emitted signal to receive all leaderboards for the
     * game in JSON format. The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.allLeaderboardsLoaded]
     * signal, contains a list of elements representing the [com.google.android.gms.games.leaderboard.Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard) class.
     *
     * @param forceReload If true, this call will clear any locally cached data and attempt to fetch
     * the latest data from the server.
     */
    @UsedByGodot
    fun loadAllLeaderboards(forceReload: Boolean) =
        leaderboardsProxy.loadAllLeaderboards(forceReload)

    /**
     * Call this method and subscribe to the emitted signal to receive the specific leaderboard in
     * JSON format. The JSON received from the [com.jacobibanez.plugin.android.godotplaygameservices.signals.LeaderboardSignals.leaderboardLoaded]
     * signal, contains a representation of the [com.google.android.gms.games.leaderboard.Leaderboard](https://developers.google.com/android/reference/com/google/android/gms/games/leaderboard/Leaderboard) class.
     *
     * @param leaderboardId The leaderboard id.
     * @param forceReload If true, this call will clear any locally cached data and attempt to fetch
     * the latest data from the server.
     */
    @UsedByGodot
    fun loadLeaderboard(leaderboardId: String, forceReload: Boolean) =
        leaderboardsProxy.loadLeaderboard(leaderboardId, forceReload)

    @UsedByGodot
    fun loadFriends(pageSize: Int, forceReload: Boolean) =
        friendsProxy.loadFriends(pageSize, forceReload)

    @UsedByGodot
    fun compareProfile(otherPlayerId: String) = friendsProxy.compareProfile(otherPlayerId)

    @UsedByGodot
    fun compareProfileWithAlternativeNameHints(
        otherPlayerId: String,
        otherPlayerInGameName: String,
        currentPlayerInGameName: String
    ) = friendsProxy.compareProfileWithAlternativeNameHints(
        otherPlayerId,
        otherPlayerInGameName,
        currentPlayerInGameName
    )
}

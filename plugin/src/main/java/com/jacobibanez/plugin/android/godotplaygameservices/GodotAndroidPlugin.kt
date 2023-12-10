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
 * will find all the methods that can be called in your game via GDScript or C#.
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
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.SignInSignals.isUserAuthenticated] signal.
     */
    @UsedByGodot
    fun isAuthenticated() = signInProxy.isAuthenticated()

    /**
     * Use this method to provide a manual way to the user for signing in.
     *
     * The method emits the [com.jacobibanez.plugin.android.godotplaygameservices.signals.SignInSignals.isUserSignedIn] signal.
     */
    @UsedByGodot
    fun signIn() = signInProxy.signIn()

    @UsedByGodot
    fun incrementAchievement(achievementId: String, amount: Int) =
        achievementsProxy.incrementAchievement(achievementId, amount)

    @UsedByGodot
    fun loadAchievements(forceReload: Boolean) =
        achievementsProxy.loadAchievements(forceReload)

    @UsedByGodot
    fun revealAchievement(achievementId: String) =
        achievementsProxy.revealAchievement(achievementId)

    @UsedByGodot
    fun showAchievements() = achievementsProxy.showAchievements()

    @UsedByGodot
    fun unlockAchievement(achievementId: String) =
        achievementsProxy.unlockAchievement(achievementId)

    @UsedByGodot
    fun showAllLeaderboards() = leaderboardsProxy.showAllLeaderboards()

    @UsedByGodot
    fun showLeaderboard(leaderboardId: String) =
        leaderboardsProxy.showLeaderboard(leaderboardId)

    @UsedByGodot
    fun showLeaderboardForTimeSpan(leaderboardId: String, timeSpan: Int) =
        leaderboardsProxy.showLeaderboardForTimeSpan(leaderboardId, timeSpan)

    @UsedByGodot
    fun showLeaderboardForTimeSpanAndCollection(
        leaderboardId: String,
        timeSpan: Int,
        collection: Int
    ) = leaderboardsProxy.showLeaderboardForTimeSpanAndCollection(
        leaderboardId, timeSpan, collection
    )

    @UsedByGodot
    fun submitScore(leaderboardId: String, score: Int) =
        leaderboardsProxy.submitScore(leaderboardId, score)

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
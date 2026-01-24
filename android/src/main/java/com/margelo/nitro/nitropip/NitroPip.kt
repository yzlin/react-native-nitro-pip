package com.margelo.nitro.nitropip

import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.os.Build
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.core.app.PictureInPictureModeChangedInfo
import androidx.core.util.Consumer
import com.facebook.proguard.annotations.DoNotStrip
import com.margelo.nitro.NitroModules
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.roundToInt

@DoNotStrip
class NitroPip : HybridNitroPipSpec() {
  private val pipModeListeners = ConcurrentHashMap<Int, (Boolean) -> Unit>()
  private val pipModeListenerId = AtomicInteger(0)
  private var pipModeChangedListener: Consumer<PictureInPictureModeChangedInfo>? = null
  private var pipModeChangedActivity: ComponentActivity? = null

  override fun isPipSupported(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return false
    }

    val context = NitroModules.applicationContext ?: return false
    val activity = context.currentActivity ?: return false

    return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
  }

  override fun isInPip(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return false
    }

    val context = NitroModules.applicationContext ?: return false
    val activity = context.currentActivity ?: return false

    return activity.isInPictureInPictureMode
  }

  override fun enterPip(options: EnterPipOptions?): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return false
    }

    val context = NitroModules.applicationContext ?: return false
    val activity = context.currentActivity ?: return false

    if (!activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
      return false
    }

    ensurePipModeChangedListenerRegistered()

    val paramsBuilder = PictureInPictureParams.Builder()

    options?.aspectRatio?.let { aspectRatio ->
      val width = aspectRatio.width
      val height = aspectRatio.height

      if (!width.isFinite() || !height.isFinite() || width <= 0 || height <= 0) {
        return false
      }

      val widthInt = width.roundToInt()
      val heightInt = height.roundToInt()
      val rational = Rational(widthInt, heightInt)
      paramsBuilder.setAspectRatio(rational)
    }

    return activity.enterPictureInPictureMode(paramsBuilder.build())
  }

  override fun addPipModeChangedListener(listener: (Boolean) -> Unit): Double {
    val id = pipModeListenerId.getAndIncrement()
    pipModeListeners[id] = listener
    ensurePipModeChangedListenerRegistered()
    return id.toDouble()
  }

  override fun removePipModeChangedListener(id: Double) {
    pipModeListeners.remove(id.toInt())
    if (pipModeListeners.isEmpty()) {
      unregisterPipModeChangedListener()
    }
  }

  private fun ensurePipModeChangedListenerRegistered() {
    if (pipModeChangedListener != null) {
      return
    }

    if (pipModeListeners.isEmpty()) {
      return
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return
    }

    val context = NitroModules.applicationContext ?: return
    val activity = context.currentActivity as? ComponentActivity ?: return

    val listener =
      Consumer<PictureInPictureModeChangedInfo> { info ->
        notifyPipModeChanged(info.isInPictureInPictureMode)
      }

    activity.addOnPictureInPictureModeChangedListener(listener)
    pipModeChangedListener = listener
    pipModeChangedActivity = activity
  }

  private fun unregisterPipModeChangedListener() {
    val activity = pipModeChangedActivity ?: return
    val listener = pipModeChangedListener ?: return

    activity.removeOnPictureInPictureModeChangedListener(listener)
    pipModeChangedListener = null
    pipModeChangedActivity = null
  }

  private fun notifyPipModeChanged(isInPip: Boolean) {
    for (listener in pipModeListeners.values) {
      listener(isInPip)
    }
  }
}

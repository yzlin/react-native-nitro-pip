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

  override fun isPiPSupported(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return false
    }

    val context = NitroModules.applicationContext ?: return false
    val activity = context.currentActivity ?: return false

    return activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
  }

  override fun isInPiP(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return false
    }

    val context = NitroModules.applicationContext ?: return false
    val activity = context.currentActivity ?: return false

    return activity.isInPictureInPictureMode
  }

  override fun enterPiP(options: EnterPiPOptions?): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      return false
    }

    val context = NitroModules.applicationContext ?: return false
    val activity = context.currentActivity ?: return false

    if (!activity.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
      return false
    }

    ensurePiPModeChangedListenerRegistered()

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

  override fun addPiPModeChangedListener(listener: (Boolean) -> Unit): Double {
    val id = pipModeListenerId.getAndIncrement()
    pipModeListeners[id] = listener
    ensurePiPModeChangedListenerRegistered()
    return id.toDouble()
  }

  override fun removePiPModeChangedListener(id: Double) {
    pipModeListeners.remove(id.toInt())
    if (pipModeListeners.isEmpty()) {
      unregisterPiPModeChangedListener()
    }
  }

  private fun ensurePiPModeChangedListenerRegistered() {
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
        notifyPiPModeChanged(info.isInPictureInPictureMode)
      }

    activity.addOnPictureInPictureModeChangedListener(listener)
    pipModeChangedListener = listener
    pipModeChangedActivity = activity
  }

  private fun unregisterPiPModeChangedListener() {
    val activity = pipModeChangedActivity ?: return
    val listener = pipModeChangedListener ?: return

    activity.removeOnPictureInPictureModeChangedListener(listener)
    pipModeChangedListener = null
    pipModeChangedActivity = null
  }

  private fun notifyPiPModeChanged(isInPiP: Boolean) {
    for (listener in pipModeListeners.values) {
      listener(isInPiP)
    }
  }
}

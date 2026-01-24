package com.margelo.nitro.nitropip

import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.os.Build
import android.util.Rational
import com.facebook.proguard.annotations.DoNotStrip
import com.margelo.nitro.NitroModules
import kotlin.math.roundToInt

@DoNotStrip
class NitroPip : HybridNitroPipSpec() {
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
}

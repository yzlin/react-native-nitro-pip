package com.margelo.nitro.nitropip
  
import com.facebook.proguard.annotations.DoNotStrip

@DoNotStrip
class NitroPip : HybridNitroPipSpec() {
  override fun multiply(a: Double, b: Double): Double {
    return a * b
  }
}

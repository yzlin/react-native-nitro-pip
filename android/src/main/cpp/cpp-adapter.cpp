#include <jni.h>
#include "nitropipOnLoad.hpp"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*) {
  return margelo::nitro::nitropip::initialize(vm);
}

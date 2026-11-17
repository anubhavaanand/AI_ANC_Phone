# R8 / ProGuard rules for the release build.
#
# The RNNoise bridge uses JNI: the Kotlin `external` methods in
# com.example.aianc.RNNoise are bound by symbol name to
# Java_com_example_aianc_RNNoise_* in libaianc.so (app/src/main/cpp/native-lib.cpp).
# R8 must not rename or strip these members, otherwise JNI lookup fails
# at runtime with UnsatisfiedLinkError.

-keepclasseswithmembernames class com.example.aianc.RNNoise {
    native <methods>;
}

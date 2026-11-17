# R8 / ProGuard rules for the release build.
#
# The RNNoise bridge uses JNI: the Kotlin `external` methods in
# com.example.aianc.RNNoise are bound by symbol name to
# Java_com_example_aianc_RNNoise_create,
# Java_com_example_aianc_RNNoise_destroy and
# Java_com_example_aianc_RNNoise_processFrame in libaianc.so
# (app/src/main/cpp/native-lib.cpp). Both the class name and the
# native method names are part of those symbols: R8 must neither
# rename nor remove them, or JNI lookup fails at runtime with
# UnsatisfiedLinkError.

-keep class com.example.aianc.RNNoise {
    native <methods>;
}

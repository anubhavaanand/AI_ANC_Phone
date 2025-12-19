#include <jni.h>
#include <string>
#include "rnnoise.h"

extern "C" JNIEXPORT jlong JNICALL
Java_com_example_aianc_RNNoise_create(JNIEnv *env, jobject thiz) {
    DenoiseState *st = rnnoise_create(NULL);
    return (jlong)st;
}

extern "C" JNIEXPORT void JNICALL
Java_com_example_aianc_RNNoise_destroy(JNIEnv *env, jobject thiz, jlong handle) {
    DenoiseState *st = (DenoiseState *)handle;
    if (st) {
        rnnoise_destroy(st);
    }
}

extern "C" JNIEXPORT jfloat JNICALL
Java_com_example_aianc_RNNoise_processFrame(JNIEnv *env, jobject thiz, jlong handle, jfloatArray in_out) {
    DenoiseState *st = (DenoiseState *)handle;
    if (!st) return 0.0f;

    jfloat *buffer = env->GetFloatArrayElements(in_out, NULL);
    float vad = rnnoise_process_frame(st, buffer, buffer);
    env->ReleaseFloatArrayElements(in_out, buffer, 0);
    
    return vad;
}

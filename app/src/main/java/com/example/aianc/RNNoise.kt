package com.example.aianc

class RNNoise {
    private var handle: Long = 0

    init {
        System.loadLibrary("aianc")
        handle = create()
    }

    fun release() {
        if (handle != 0L) {
            destroy(handle)
            handle = 0
        }
    }

    fun process(frame: FloatArray): Float {
        if (handle == 0L) return 0f
        return processFrame(handle, frame)
    }

    private external fun create(): Long
    private external fun destroy(handle: Long)
    private external fun processFrame(handle: Long, inOut: FloatArray): Float

    companion object {
        const val FRAME_SIZE = 480 // RNNoise frame size
    }
}

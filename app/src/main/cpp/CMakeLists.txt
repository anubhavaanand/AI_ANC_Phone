cmake_minimum_required(VERSION 3.22.1)

project("aianc")

# Add RNNoise sources
set(RNNOISE_DIR rnnoise)
set(RNNOISE_SOURCES
    ${RNNOISE_DIR}/src/denoise.c
    ${RNNOISE_DIR}/src/rnn.c
    ${RNNOISE_DIR}/src/rnn_data.c
    ${RNNOISE_DIR}/src/pitch.c
    ${RNNOISE_DIR}/src/kiss_fft.c
    ${RNNOISE_DIR}/src/celt_lpc.c
)

add_library(
    aianc
    SHARED
    native-lib.cpp
    ${RNNOISE_SOURCES}
)

target_include_directories(
    aianc
    PRIVATE
    ${RNNOISE_DIR}/include
    ${RNNOISE_DIR}/src
)

# Use -lm for math library
target_link_libraries(
    aianc
    log
    m
)

#include <jni.h>
#include <stdlib.h>

#define NOISE_SIZE 30

int randomNoise() {
    return rand() % NOISE_SIZE;
}

int* extractColor(int color) {
    static int colorParts[4];
    colorParts[0] = (color >> 16) & 0xFF;   // R
    colorParts[1] = (color >> 8) & 0xFF;    // G
    colorParts[2] = (color) & 0xFF;         // B
    colorParts[3] = (color >> 24) & 0xFF;   // A
    return colorParts;
}

float translateColorToHeight(int color) {
    int *colorParts = extractColor(color);
    return (((colorParts[0] + colorParts[1] + colorParts[2]) / 3) * randomNoise()) / 255;
}

float* createVertexBufferFlat(jint* positions, int positionsCount, jint* imageData, jint imageSize) {
    float *vertexBuffer = malloc(positionsCount*sizeof(float));
    int index = 0;
    for (int i = 0; i < positionsCount; i += 3) {
        int positionX = positions[index++];
        int positionY = positions[index++];
        vertexBuffer[i + 0] = positionX;
        vertexBuffer[i + 1] = positionY;

        int colorValue = imageData[positionY * imageSize + positionX];
        float height = translateColorToHeight(colorValue);
        vertexBuffer[i + 2] = height;
    }
    return vertexBuffer;
}

float* createColorBufferFlat(jint* positions, int positionsCount, jint* imageData, jint imageSize) {
    float *colorBuffer = malloc(positionsCount*sizeof(float));
    int index = 0;
    for (int i = 0; i < positionsCount; i += 4) {
        int positionX = positions[index++];
        int positionY = positions[index++];
        int colorValue = imageData[positionY * imageSize + positionX];
        int* extractedColor = extractColor(colorValue);
        for (int j = 0; j <= 3; j++) {
            colorBuffer[i + j] = extractedColor[j] / 255.0;
        }
    }
    return colorBuffer;
}

JNIEXPORT jfloat JNICALL
Java_com_mlauncher_gl_NativeBufferCreator_translateColorToHeight(JNIEnv *env,
                                                                    jobject instance,
                                                                    jint color) {
    float height = translateColorToHeight(color);
    return height;
}

JNIEXPORT jintArray JNICALL
Java_com_mlauncher_gl_NativeBufferCreator_extractColor(JNIEnv *env,
                                                          jobject instance,
                                                          jint color) {
    jintArray result = (*env)->NewIntArray(env, 4);
    int *colorParts = extractColor(color);
    (*env)->SetIntArrayRegion(env, result, 0, 4, colorParts);
    return result;
}

JNIEXPORT jint JNICALL
Java_com_mlauncher_gl_NativeBufferCreator_randomNoise(JNIEnv *env,
                                                         jobject instance) {
    return randomNoise();
}


JNIEXPORT jfloatArray JNICALL
Java_com_mlauncher_gl_NativeBufferCreator_createVertexBufferFlat(JNIEnv *env,
                                                                    jobject instance,
                                                                    jintArray positions_,
                                                                    jintArray imageData_,
                                                                    jint imageSize) {
    const jsize positionsLength = (*env)->GetArrayLength(env, positions_);
    jint *positions = (*env)->GetIntArrayElements(env, positions_, NULL);
    jint *imageData = (*env)->GetIntArrayElements(env, imageData_, NULL);

    int positionsCount = (positionsLength / 2) * 3;
    float *vertexBuffer = createVertexBufferFlat(positions, positionsCount, imageData, imageSize);

    jfloatArray result = (*env)->NewFloatArray(env, positionsCount);

    (*env)->ReleaseIntArrayElements(env, positions_, positions, 0);
    (*env)->ReleaseIntArrayElements(env, imageData_, imageData, 0);
    (*env)->SetFloatArrayRegion(env, result, 0, positionsCount, vertexBuffer);

    memset(vertexBuffer, 0, sizeof(*vertexBuffer));
    free(vertexBuffer);

    return result;
}

JNIEXPORT jfloatArray JNICALL
Java_com_mlauncher_gl_NativeBufferCreator_createColorBufferFlat(JNIEnv *env,
                                                                   jobject instance,
                                                                   jintArray positions_,
                                                                   jintArray imageData_,
                                                                   jint imageSize) {
    const jsize positionsLength = (*env)->GetArrayLength(env, positions_);
    jint *positions = (*env)->GetIntArrayElements(env, positions_, NULL);
    jint *imageData = (*env)->GetIntArrayElements(env, imageData_, NULL);

    int positionsCount = positionsLength * 2;
    float *colorBuffer = createColorBufferFlat(positions, positionsCount, imageData, imageSize);

    jfloatArray result = (*env)->NewFloatArray(env, positionsCount);

    (*env)->ReleaseIntArrayElements(env, positions_, positions, 0);
    (*env)->ReleaseIntArrayElements(env, imageData_, imageData, 0);
    (*env)->SetFloatArrayRegion(env, result, 0, positionsCount, colorBuffer);

    memset(colorBuffer, 0, sizeof(*colorBuffer));
    free(colorBuffer);

    return result;
}

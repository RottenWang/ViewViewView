package com.drwang.views.util;

import android.graphics.PointF;
import android.media.ExifInterface;

import com.drwang.views.bean.FilterInfo;

import java.io.IOException;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCGAColorspaceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBalanceFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBurnBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorDodgeBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorMatrixFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDarkenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDifferenceBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDivideBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExclusionBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageExposureFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFalseColorFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHalftoneFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHardLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLaplacianFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLevelsFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLightenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLinearBurnBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLookupFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageLuminosityBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMonochromeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNonMaximumSuppressionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageNormalBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageScreenBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSharpenFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSmoothToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSobelEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageSoftLightBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSourceOverBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSphereRefractionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSubtractBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSwirlFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageThresholdEdgeDetection;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageTransformFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageVignetteFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWeakPixelInclusionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

/**
 * Created by Administrator on 2017/8/18.
 */

public class FilterUtil {
    private FilterUtil() {
    }

    public static void getFilterInfo(List<FilterInfo> mFilterList) {
        mFilterList.clear();
        mFilterList.add(new FilterInfo(new GPUImageFilter(), "original"));
        mFilterList.add(new FilterInfo(new GPUImage3x3ConvolutionFilter(new float[]{1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f}), "3x3Convo"));
        mFilterList.add(new FilterInfo(new GPUImageAddBlendFilter(), "addblend"));
        mFilterList.add(new FilterInfo(new GPUImageAlphaBlendFilter(0.5f), "alphaBlend", 0.5f, 0, 1, (p, f) -> {
            ((GPUImageAlphaBlendFilter) f).setMix(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageBilateralFilter(0.1f), "Bilateral", 0.1f, 0, 10, (p, f) -> {
            ((GPUImageBilateralFilter) f).setDistanceNormalizationFactor(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageBoxBlurFilter(5f), "BoxBlur", 5f, 0, 10, (p, f) -> {
            ((GPUImageBoxBlurFilter) f).setBlurSize(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageBrightnessFilter(0.3f), "Brightness", 0.3f, 0, 1, (p, f) -> {
            ((GPUImageBrightnessFilter) f).setBrightness(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageCGAColorspaceFilter(), "CGAColor"));
        mFilterList.add(new FilterInfo(new GPUImageChromaKeyBlendFilter(), "ChromaKey"));
        mFilterList.add(new FilterInfo(new GPUImageColorBalanceFilter(), "ColorBalance"));
        mFilterList.add(new FilterInfo(new GPUImageColorBlendFilter(), "ColorBlend"));
//        mFilterList.add(new FilterInfo(new GPUImageColorBurnBlendFilter(), "ColorBurn"));
        mFilterList.add(new FilterInfo(new GPUImageColorDodgeBlendFilter(), "ColorDodge"));
        mFilterList.add(new FilterInfo(new GPUImageColorInvertFilter(), "ColorInvert"));
        mFilterList.add(new FilterInfo(new GPUImageColorMatrixFilter(), "ColorMatrix"));
        mFilterList.add(new FilterInfo(new GPUImageContrastFilter(1.2f), "Contrast", 1.2f, 0, 5, (p, f) -> {
            ((GPUImageContrastFilter) f).setContrast(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageCrosshatchFilter(0.01f, 0.0005f), "Crosshatch", 0.01f, 0, 0.05f, (p, f) -> {
            ((GPUImageCrosshatchFilter) f).setCrossHatchSpacing(p);
        }));
//        mFilterList.add(new FilterInfo(new GPUImageDarkenBlendFilter(), "DarkenBlend"));
        mFilterList.add(new FilterInfo(new GPUImageDifferenceBlendFilter(), "DifferenceBlend"));
        mFilterList.add(new FilterInfo(new GPUImageDilationFilter(1), "Dilation1"));

        mFilterList.add(new FilterInfo(new GPUImageDirectionalSobelEdgeDetectionFilter(), "DirectionSobel"));
        mFilterList.add(new FilterInfo(new GPUImageDissolveBlendFilter(), "DissolveBlend"));
        mFilterList.add(new FilterInfo(new GPUImageDivideBlendFilter(), "DivideBlend"));
        mFilterList.add(new FilterInfo(new GPUImageEmbossFilter(1.0f), "Emboss", 1.0f, 0, 10, (p, f) -> {
            ((GPUImageEmbossFilter) f).setIntensity(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageExclusionBlendFilter(), "ExclusionBlend"));
        mFilterList.add(new FilterInfo(new GPUImageExposureFilter(1.0f), "Exposure", 1.0f, 0, 8, (p, f) -> {
            ((GPUImageExposureFilter) f).setExposure(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageFalseColorFilter(), "FalseColor"));
        mFilterList.add(new FilterInfo(new GPUImageGammaFilter(1.2f), "Gamma", 1.2f, 0, 10, (p, f) -> {
            ((GPUImageGammaFilter) f).setGamma(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageGaussianBlurFilter(1f), "GaussianBlur", 1f, 0, 20, (p, f) -> {
            ((GPUImageGaussianBlurFilter) f).setBlurSize(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageGrayscaleFilter(), "Grayscale"));
        mFilterList.add(new FilterInfo(new GPUImageHalftoneFilter(0.01f), "Halftone", 0.01f, 0.0001f, 0.03f, (p, f) -> {
            ((GPUImageHalftoneFilter) f).setFractionalWidthOfAPixel(p);

        }));
//        mFilterList.add(new FilterInfo(new GPUImageHardLightBlendFilter(), "HardLight"));
        mFilterList.add(new FilterInfo(new GPUImageHazeFilter(0.2f, 0), "Haze", 0.2f, 0, 0.99f, (p, f) -> {
            ((GPUImageHazeFilter) f).setDistance(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageHighlightShadowFilter(0, -5), "HighlightShadow", -5, 0, 10, (p, f) -> {
            ((GPUImageHighlightShadowFilter) f).setShadows(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageHueBlendFilter(), "HueBlend"));
        mFilterList.add(new FilterInfo(new GPUImageKuwaharaFilter(3), "Kuwahara", 3, 0, 30, (p, f) -> {
            ((GPUImageKuwaharaFilter) f).setRadius((int) p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageLaplacianFilter(), "Laplacian"));
        mFilterList.add(new FilterInfo(new GPUImageLevelsFilter(), "Levels"));
        mFilterList.add(new FilterInfo(new GPUImageLightenBlendFilter(), "LightenBlend"));
//        mFilterList.add(new FilterInfo(new GPUImageLinearBurnBlendFilter(), "LinearBurnBlend"));
        mFilterList.add(new FilterInfo(new GPUImageLookupFilter(0), "Loopup", 1, -5, 0, (p, f) -> {
            ((GPUImageLookupFilter) f).setIntensity(p);
        }));
//        mFilterList.add(new FilterInfo(new GPUImageLuminosityBlendFilter(), "LuminosityBlend"));
//        mFilterList.add(new FilterInfo(new GPUImageMixBlendFilter()));
        mFilterList.add(new FilterInfo(new GPUImageMonochromeFilter(), "Monochrome"));
//        mFilterList.add(new FilterInfo(new GPUImageMultiplyBlendFilter(), "MultiplyBlend"));
        mFilterList.add(new FilterInfo(new GPUImageNonMaximumSuppressionFilter(), "NonMaximumSuppression"));
//        mFilterList.add(new FilterInfo(new GPUImageNormalBlendFilter(), "NormalBlend"));
        mFilterList.add(new FilterInfo(new GPUImageOpacityFilter(), "Opacity"));
        mFilterList.add(new FilterInfo(new GPUImageOverlayBlendFilter(), "OverlayBlend"));
        mFilterList.add(new FilterInfo(new GPUImagePixelationFilter(), "Pixelation"));
        mFilterList.add(new FilterInfo(new GPUImagePosterizeFilter(10), "Posterize", 10, 1, 10, (p, f) -> {
            ((GPUImagePosterizeFilter) f).setColorLevels((int) p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageRGBDilationFilter(1), "RGBDilation"));
        mFilterList.add(new FilterInfo(new GPUImageRGBFilter(1, 0.5f, 1), "泛红", 0.5f, 0, 1, (p, f) -> {
//            ((GPUImageRGBFilter) f).setBlue(p);
//            ((GPUImageRGBFilter) f).setRed(p);
            ((GPUImageRGBFilter) f).setGreen(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageRGBFilter(0.5f, 1, 1), "泛蓝", 0.5f, 0, 1, (p, f) -> {
//            ((GPUImageRGBFilter) f).setBlue(p);
            ((GPUImageRGBFilter) f).setRed(p);
//            ((GPUImageRGBFilter) f).setGreen(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageRGBFilter(1, 1, 0.5f), "泛黄", 0.5f, 0, 1, (p, f) -> {
            ((GPUImageRGBFilter) f).setBlue(p);
//            ((GPUImageRGBFilter) f).setRed(p);
//            ((GPUImageRGBFilter) f).setGreen(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageSaturationBlendFilter(), "饱和"));
        mFilterList.add(new FilterInfo(new GPUImageSaturationFilter(), "饱和2", 1, -20, 20, (p, f) -> {
            ((GPUImageSaturationFilter) f).setSaturation(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageScreenBlendFilter(), "ScreenBlend"));
        mFilterList.add(new FilterInfo(new GPUImageSepiaFilter(), "Sepia", 1, -20, 20, (p, f) -> {
            ((GPUImageSepiaFilter) f).setIntensity(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageSharpenFilter(), "Sharpen", 0, -10, 10, (p, f) -> {
            ((GPUImageSharpenFilter) f).setSharpness(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageSketchFilter(), "Sketch"));
        mFilterList.add(new FilterInfo(new GPUImageSmoothToonFilter(), "SmoothToon"));
        mFilterList.add(new FilterInfo(new GPUImageSobelEdgeDetection(), "SobelEdge"));
        mFilterList.add(new FilterInfo(new GPUImageSoftLightBlendFilter(), "SoftLightBlend"));
//        mFilterList.add(new FilterInfo(new GPUImageSourceOverBlendFilter(), "SourceOverBlend"));
        mFilterList.add(new FilterInfo(new GPUImageSphereRefractionFilter(new PointF(0.5f, 0.5f), 0.5f, 0.71f), "SphereRefraction", 0.75f, -10, 10, (p, f) -> {
            ((GPUImageSphereRefractionFilter) f).setRefractiveIndex(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageSubtractBlendFilter(), "SubtractBlend"));
        mFilterList.add(new FilterInfo(new GPUImageSwirlFilter(), "Swirl", 0.5f, 0, 20, (p, f) -> {
            ((GPUImageSwirlFilter) f).setRadius(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageThresholdEdgeDetection(), "ThresholdEdge"));
        mFilterList.add(new FilterInfo(new GPUImageToneCurveFilter(), "ToneCurver"));
        mFilterList.add(new FilterInfo(new GPUImageToonFilter(), "Toon", 0.2f, 0.01f, 1, (p, f) -> {
            ((GPUImageToonFilter) f).setThreshold(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageTransformFilter(), "Transform"));
//        mFilterList.add(new FilterInfo(new GPUImageTwoInputFilter()));
//        mFilterList.add(new FilterInfo(new GPUImageTwoPassFilter()));
//        mFilterList.add(new FilterInfo(new GPUImageTwoPassTextureSamplingFilter()));
        mFilterList.add(new FilterInfo(new GPUImageVignetteFilter(), "Vigenette", 0.3f, 0, 1, (p, f) -> {
            ((GPUImageVignetteFilter) f).setVignetteStart(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageWeakPixelInclusionFilter(), "WeakPixelInclusion"));
        mFilterList.add(new FilterInfo(new GPUImageWhiteBalanceFilter(), "WhiteBalance", 5000.0f, 0, 10000, (p, f) -> {
            ((GPUImageWhiteBalanceFilter) f).setTemperature(p);
        }));
    }

    public static int getImageDegree(String filepath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        int degree = 0;
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;

            }
        }
        return degree;
    }

}

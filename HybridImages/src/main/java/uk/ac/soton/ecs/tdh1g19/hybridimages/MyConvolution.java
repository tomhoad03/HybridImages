package uk.ac.soton.ecs.tdh1g19.hybridimages;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
    private final float[][] kernel;

    public MyConvolution(float[][] kernel) {
        this.kernel = kernel;
    }

    @Override
    public void processImage(FImage image) {
        int kernelHeight = kernel.length;
        int kernelWidth = kernel[0].length;

        int imageHeight = image.getHeight();
        int imageWidth = image.getWidth();

        FImage original = image.internalAssign(image);

        // search for pixels not at the boundary
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                float result = 0;
                int p = 0;

                for (int m = j - ((kernelWidth - 1) / 2); m < j + ((kernelWidth - 1) / 2) - 1; m++) {
                    int q = 0;
                    for (int n = i - ((kernelHeight - 1) / 2); n < i + ((kernelHeight - 1) / 2) - 1; n++) {
                        if (m >= 0 && n >= 0 && m < imageWidth && n < imageHeight) {
                            float template = kernel[p][q];
                            float pixel = original.getPixel(m, n);
                            result += template * pixel;
                        }
                        q++;
                    }
                    p++;
                }
                image.setPixel(j, i, result);
            }
        }
    }
}
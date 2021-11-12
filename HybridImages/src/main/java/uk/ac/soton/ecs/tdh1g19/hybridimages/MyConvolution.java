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
        try {
            int kernelHeight = kernel.length;
            int kernelWidth = kernel[0].length;

            // Zero padding the image
            FImage padded = image.padding((kernelWidth - 1) / 2, (kernelHeight - 1) / 2, (float) 0);
            FImage clone = padded.clone();

            int imageHeight = padded.getHeight();
            int imageWidth = padded.getWidth();

            // Look for pixels part of the original image
            for (int i = (kernelHeight - 1) / 2; i < imageHeight - ((kernelHeight - 1) / 2); i++) {
                for (int j = (kernelWidth - 1) / 2; j < imageWidth - ((kernelWidth - 1) / 2); j++) {
                    float result = 0;

                    // Apply the template to the surrounding pixels to get a new value for the centre pixel
                    for (int m = -((kernelHeight - 1) / 2); m < (kernelHeight - 1) / 2; m++) {
                        for (int n = -((kernelWidth - 1) / 2); n < (kernelWidth - 1) / 2; n++) {
                            result += kernel[m + ((kernelHeight - 1) / 2)][n + ((kernelWidth - 1) / 2)] * clone.getPixel(j + n, i + m);
                        }
                    }
                    image.setPixel(j - ((kernelWidth - 1) / 2), i - ((kernelHeight - 1) / 2), result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
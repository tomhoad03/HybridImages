package uk.ac.soton.ecs.tdh1g19.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;

import java.io.File;
import java.util.Objects;

public class MyHybridImages {
    /**
     * Compute a hybrid image combining low-pass and high-pass filtered images
     *
     * @param lowImage
     *            the image to which apply the low pass filter
     * @param lowSigma
     *            the standard deviation of the low-pass filter
     * @param highImage
     *            the image to which apply the high pass filter
     * @param highSigma
     *            the standard deviation of the low-pass component of computing the
     *            high-pass filtered image
     * @return the computed hybrid image
     */
    public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {
        try {
            // Applies a low pass filter by convolution
            int lowPassSize = (int) (8.0f * lowSigma + 1.0f);
            if (lowPassSize % 2 == 0) lowPassSize++;
            float[][] lowPassKernel = new float[lowPassSize][lowPassSize];
            lowImage.processInplace(new MyConvolution(lowPassKernel));

            // Applies a high pass filter by convolution
            int highPassSize = (int) (8.0f * highSigma + 1.0f);
            if (highPassSize % 2 == 0) highPassSize++;
            float[][] highPassKernel = new float[highPassSize][highPassSize];
            // highImage.processInplace(new MyConvolution(highPassKernel));

            return lowImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            MBFImage catImage = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\cat.bmp"));
            MBFImage dogImage = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\dog.bmp"));

            DisplayUtilities.display(Objects.requireNonNull(makeHybrid(catImage, 1, dogImage, 1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
      To do:
      1. Create a convolution algorithm
      2. Apply a low pass filter to an image
      3. Apply a high pass filter to an image
      4. Sum two images together
     */
}
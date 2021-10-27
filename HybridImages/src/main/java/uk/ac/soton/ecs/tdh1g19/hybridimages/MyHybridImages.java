package uk.ac.soton.ecs.tdh1g19.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;

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
            // Determines the size of the kernel
            int lowPassSize = (int) (8.0f * lowSigma + 1.0f);
            if (lowPassSize % 2 == 0) lowPassSize++;

            int highPassSize = (int) (8.0f * highSigma + 1.0f);
            if (highPassSize % 2 == 0) highPassSize++;

            // Creates two low pass filter templates
            FImage lowPassFilter = Gaussian2D.createKernelImage(lowPassSize, lowSigma);
            FImage highPassFilter = Gaussian2D.createKernelImage(highPassSize, highSigma);

            // Applies a low pass filter by convolution
            lowImage.processInplace(new MyConvolution(lowPassFilter.pixels));

            // Applies a high pass filter by convolution and subtraction
            MBFImage originalHighImage = highImage.clone();
            highImage.processInplace(new MyConvolution(highPassFilter.pixels));
            MBFImage newHighImage = originalHighImage.subtract(highImage);

            return lowImage.add(newHighImage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            MBFImage catImage = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\cat.bmp"));
            MBFImage dogImage = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\dog.bmp"));

            DisplayUtilities.display(Objects.requireNonNull(makeHybrid(dogImage, 5, catImage, 5)));
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
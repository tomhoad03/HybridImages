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
            // Creates an empty template
            int lowPassSize = (int) (8.0f * lowSigma + 1.0f);
            if (lowPassSize % 2 == 0) lowPassSize++;
            float[][] lowPassKernel = new float[lowPassSize][lowPassSize];

            // Creates a low pass filter template
            for (int i = 0; i < lowPassKernel.length; i++) {
                for (int j = 0; j < lowPassKernel[0].length; j++) {
                    lowPassKernel[i][j] = (float) 0.005;
                }
            }

            // Applies a low pass filter by convolution
            lowImage.processInplace(new MyConvolution(new float[][]{{1, 0, -1}, {2, 0, -2}, {1, 0, -1}}));
            lowImage.processInplace(new MyConvolution(new float[][]{{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}}));

            // Creates an empty template
            int highPassSize = (int) (8.0f * highSigma + 1.0f);
            if (highPassSize % 2 == 0) highPassSize++;
            float[][] highPassKernel = new float[highPassSize][highPassSize];

            // Creates a high pass filter template

            // Applies a high pass filter by convolution
            //highImage.processInplace(new MyConvolution(highPassKernel));

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
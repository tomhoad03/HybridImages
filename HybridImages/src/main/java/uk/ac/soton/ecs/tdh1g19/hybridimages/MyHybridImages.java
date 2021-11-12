package uk.ac.soton.ecs.tdh1g19.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.image.processing.resize.ResizeProcessor;

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

            return lowImage.add(originalHighImage.subtract(highImage));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            MBFImage imageA = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\dog.bmp"));
            MBFImage imageB = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\cat.bmp"));
            MBFImage blank = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\blank.bmp"));

            MBFImage hybrid = makeHybrid(imageA, 5, imageB, 5);

            blank.drawImage(hybrid, 0, 0);
            blank.drawImage(ResizeProcessor.halfSize(Objects.requireNonNull(hybrid).clone()), 532, 256);
            blank.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybrid.clone())), 808, 384);
            blank.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybrid.clone()))), 956, 448);
            blank.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybrid.clone())))), 1040, 480);

            DisplayUtilities.display(hybrid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
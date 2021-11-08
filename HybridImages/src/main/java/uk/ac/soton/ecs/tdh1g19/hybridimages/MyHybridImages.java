package uk.ac.soton.ecs.tdh1g19.hybridimages;

import org.checkerframework.checker.units.qual.A;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.image.processing.resize.ResizeProcessor;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
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

    public static void customHybrid() {
        try {
            MBFImage imageA = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\johnson.bmp"));
            MBFImage imageB = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\worm.bmp"));
            MBFImage blank = ImageUtilities.readMBF(new File("D:\\Documents\\Coursework2\\HybridImages\\src\\main\\java\\uk\\ac\\soton\\ecs\\tdh1g19\\hybridimages\\images\\blank.bmp"));

            MBFImage hybrid = makeHybrid(imageA, 12, imageB, 12);

            blank.drawImage(hybrid, 0, 0);
            blank.drawImage(ResizeProcessor.halfSize(Objects.requireNonNull(hybrid).clone()), 532, 256);
            blank.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybrid.clone())), 808, 384);
            blank.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybrid.clone()))), 956, 448);
            blank.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybrid.clone())))), 1040, 480);

            DisplayUtilities.display(blank);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        customHybrid();
    }
}
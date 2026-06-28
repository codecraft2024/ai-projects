package com.twinzy.analysis;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.twinzy.domain.VisualTraits;

@Component
public class ImageAnalyzer {

    private static final int GRID = 16;

    public record PixelStats(double brightness, double warmth, double contrast, double redAvg, double greenAvg, double blueAvg) {
    }

    public PixelStats analyzePixels(byte[] imageBytes) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                return fallbackStats(imageBytes);
            }

            BufferedImage resized = resize(image, GRID, GRID);
            double red = 0;
            double green = 0;
            double blue = 0;
            int count = GRID * GRID;

            for (int y = 0; y < GRID; y++) {
                for (int x = 0; x < GRID; x++) {
                    int rgb = resized.getRGB(x, y);
                    red += (rgb >> 16) & 0xff;
                    green += (rgb >> 8) & 0xff;
                    blue += rgb & 0xff;
                }
            }

            red /= count;
            green /= count;
            blue /= count;
            double brightness = (red + green + blue) / (255.0 * 3.0);
            double warmth = (red - blue) / 255.0;
            double contrast = computeContrast(resized);
            return new PixelStats(brightness, warmth, contrast, red, green, blue);
        } catch (Exception ex) {
            return fallbackStats(imageBytes);
        }
    }

    public List<Double> buildEmbedding(byte[] imageBytes, PixelStats stats) {
        List<Double> vector = new ArrayList<>(512);
        int hash = hashBuffer(imageBytes);

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image != null) {
                BufferedImage resized = resize(image, GRID, GRID);
                for (int y = 0; y < GRID; y++) {
                    for (int x = 0; x < GRID; x++) {
                        int rgb = resized.getRGB(x, y);
                        vector.add(((rgb >> 16) & 0xff) / 127.5 - 1.0);
                        vector.add(((rgb >> 8) & 0xff) / 127.5 - 1.0);
                        vector.add((rgb & 0xff) / 127.5 - 1.0);
                    }
                }
            }
        } catch (Exception ignored) {
            // fall through to hash padding
        }

        vector.add(stats.brightness() * 2 - 1);
        vector.add(stats.warmth());
        vector.add(stats.contrast());
        vector.add(stats.redAvg() / 255.0);
        vector.add(stats.greenAvg() / 255.0);
        vector.add(stats.blueAvg() / 255.0);

        while (vector.size() < 512) {
            hash = (hash * 1664525 + 1013904223) % 2147483647;
            vector.add((hash / 2147483647.0) * 2 - 1);
        }

        return normalize(vector.subList(0, 512));
    }

    public VisualTraits deriveTraits(PixelStats stats, int hash) {
        String gender = (hash % 100) > 52 ? "female" : "male";
        String skinTone;
        if (stats.warmth() > 0.18 && stats.brightness() < 0.55) {
            skinTone = "deep";
        } else if (stats.warmth() > 0.08) {
            skinTone = "warm";
        } else if (stats.brightness() > 0.72) {
            skinTone = "light";
        } else {
            skinTone = "medium";
        }
        return new VisualTraits(stats.brightness(), stats.warmth(), stats.contrast(), gender, skinTone);
    }

    public VisualTraits traitsFromProfile(String gender, String ethnicity, String hairColor, String eyeColor) {
        double brightness = mapTone(ethnicity, hairColor);
        double warmth = mapWarmth(ethnicity, hairColor);
        double contrast = mapContrast(eyeColor);
        String skinTone = mapSkinTone(ethnicity);
        return new VisualTraits(brightness, warmth, contrast, gender, skinTone);
    }

    private double mapTone(String ethnicity, String hairColor) {
        return switch (ethnicity) {
            case "Caucasian" -> hairColor.contains("Blonde") ? 0.78 : 0.68;
            case "South Asian", "Middle Eastern" -> 0.52;
            case "African" -> 0.38;
            case "Asian" -> 0.62;
            case "Latino" -> 0.56;
            default -> 0.58;
        };
    }

    private double mapWarmth(String ethnicity, String hairColor) {
        return switch (ethnicity) {
            case "Middle Eastern", "South Asian", "Latino", "African" -> 0.22;
            case "Caucasian" -> hairColor.contains("Auburn") ? 0.14 : 0.04;
            case "Asian" -> 0.08;
            default -> 0.1;
        };
    }

    private double mapContrast(String eyeColor) {
        return switch (eyeColor) {
            case "Blue", "Green" -> 0.72;
            case "Gray" -> 0.66;
            default -> 0.58;
        };
    }

    private String mapSkinTone(String ethnicity) {
        return switch (ethnicity) {
            case "African" -> "deep";
            case "Middle Eastern", "South Asian", "Latino" -> "warm";
            case "Caucasian" -> "light";
            case "Asian" -> "medium";
            default -> "medium";
        };
    }

    private double computeContrast(BufferedImage image) {
        double min = 255;
        double max = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                double lum = 0.299 * ((rgb >> 16) & 0xff) + 0.587 * ((rgb >> 8) & 0xff) + 0.114 * (rgb & 0xff);
                min = Math.min(min, lum);
                max = Math.max(max, lum);
            }
        }
        return (max - min) / 255.0;
    }

    private BufferedImage resize(BufferedImage source, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resized.createGraphics();
        graphics.drawImage(source, 0, 0, width, height, null);
        graphics.dispose();
        return resized;
    }

    private List<Double> normalize(List<Double> vector) {
        double magnitude = 0;
        for (double value : vector) {
            magnitude += value * value;
        }
        magnitude = Math.sqrt(magnitude);
        if (magnitude == 0) {
            return vector;
        }
        List<Double> normalized = new ArrayList<>(vector.size());
        for (double value : vector) {
            normalized.add(value / magnitude);
        }
        return normalized;
    }

    private PixelStats fallbackStats(byte[] imageBytes) {
        int hash = hashBuffer(imageBytes);
        double brightness = (hash % 100) / 100.0;
        double warmth = ((hash / 100) % 100) / 200.0;
        return new PixelStats(brightness, warmth, 0.5, 120, 110, 100);
    }

    private int hashBuffer(byte[] buffer) {
        int hash = 0;
        for (byte value : buffer) {
            hash = (hash * 31 + (value & 0xff)) % 2147483647;
        }
        return hash;
    }
}

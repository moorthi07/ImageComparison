package image.comparison;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Runner {

    private static Comparator<Point> copmaparatorX = (p1, p2) -> {
        int result = 0;
        if (p1.getX() == p2.getX()) {
            result = 0;
        }
        if (p1.getX() < p2.getX()) {
            result = -1;
        }
        if (p1.getX() > p2.getX()) {
            result = 1;
        }
        return result;
    };
    private static Comparator<Point> copmaparatorY = (p1, p2) -> {
        int result = 0;
        if (p1.getY() == p2.getY()) {
            result = 0;
        }
        if (p1.getY() < p2.getY()) {
            result = -1;
        }
        if (p1.getY() > p2.getY()) {
            result = 1;
        }
        return result;
    };

    public static void main(String[] args) {
        int percentOfDifference = 10;
        int radiusForGroups = 100;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        BufferedImage img1 = null;
        BufferedImage img2 = null;
        try {
            img1 = ImageIO.read(new File(classLoader.getResource("image1.png").getFile()));
            img2 = ImageIO.read(new File(classLoader.getResource("image2.png").getFile()));
        } catch (IOException e) {
            System.err.println("Exception: Check file path");
            System.exit(1);
        }
        int width1 = img1.getWidth();
        int height1 = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();
        if ((width1 != width2) || (height1 != height2)) {
            System.err.println("Exception: Images width or height does not fit");
            System.exit(1);
        }

        List<Point> diffPoints = new LinkedList<>();
        for (int i = 0; i < width1; i++) {
            for (int j = 0; j < height1; j++) {
                double a = (double) img1.getRGB(i, j);
                double b = (double) img2.getRGB(i, j);
                if (Math.round(Math.abs((a - b) / ((a + b) / 2)) * 100) >= percentOfDifference) {
                    diffPoints.add(new Point(i, j, img2.getRGB(i, j)));
                }
            }
        }
        List<List<Point>> listOfDiffGroups = new LinkedList<>();
        for (int i = 0; i < diffPoints.size(); i++) {
            List<Point> group = new LinkedList<>();
            group.add(diffPoints.get(i));
            for (int j = i + 1; j < diffPoints.size(); j++) {
                if ((Math.abs(diffPoints.get(i).getX() - diffPoints.get(j).getX()) < radiusForGroups
                        && Math.abs(diffPoints.get(i).getY() - diffPoints.get(j).getY()) < radiusForGroups)) {
                    group.add(diffPoints.remove(j));
                    j--;
                }
            }
            listOfDiffGroups.add(group);
        }

        BufferedImage image = new BufferedImage(img2.getColorModel(), img2.copyData(null),
                img2.getColorModel().isAlphaPremultiplied(), null);
        Graphics2D graph = image.createGraphics();
        graph.setColor(Color.RED);

        for (List<Point> list : listOfDiffGroups) {
            int maxX = list.stream().max(copmaparatorX).orElse(new Point(0, 0, 0)).getX();
            int minX = list.stream().min(copmaparatorX).orElse(new Point(0, 0, 0)).getX();
            int maxY = list.stream().max(copmaparatorY).orElse(new Point(0, 0, 0)).getY();
            int minY = list.stream().min(copmaparatorY).orElse(new Point(0, 0, 0)).getY();

            graph.draw(new Rectangle(minX, minY, maxX - minX, maxY - minY));
        }
        graph.dispose();
        try {
            ImageIO.write(image, "png", new File("diff.png"));
        } catch (IOException e) {
            System.err.println("Ouput IO Exception");
            System.exit(1);
        }
        System.out.println("Done! There were find " + listOfDiffGroups.size() + " different parts");
    }
}

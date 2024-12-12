package campus.ui.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import java.util.stream.IntStream;

import javax.swing.Timer;
import javax.swing.JPanel;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class CampusLogo extends JPanel implements ActionListener {
    private static final long serialVersionUID = 526441883823230697L;

    private static final int BLINKING_INTERVAL = 10000;
    private static final int BLINKING_DURATION = 300;
    private static final int BLINKING_FPS = 120;

    private static final Color HEAD_COLOR = new Color(156, 164, 52);
    private static final Color EYEBALL_COLOR = Color.WHITE;
    private static final Color PUPIL_COLOR = new Color(150, 150, 150);

    private double blinkingFactor = 1.0;

    public CampusLogo() {
        this(true);
    }

    public CampusLogo(boolean blinking) {
        if (blinking) {
            new Timer(BLINKING_INTERVAL, this).start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var frames = Math.floorDiv(BLINKING_DURATION * BLINKING_FPS, 1000);
        new Thread(() ->
            IntStream.range(0, frames).forEachOrdered(i -> {
                blinkingFactor = Math.cos(Math.PI*2*i / (frames - 1)) / 2 + 0.5;
                repaint();
                try {
                    Thread.sleep(Math.floorDiv(1000, BLINKING_FPS));
                } catch (InterruptedException ignored) {}
            })).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2d = (Graphics2D) g;

        g2d.transform(AffineTransform.getScaleInstance(
            getWidth(), getHeight()));

        var renderingHints = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(renderingHints);

        var head = new GeneralPath();
        head.moveTo(0.9, 0.1);
        head.curveTo(1.3, 1.3, 0.0, 0.9, 0.0, 0.4);
        head.curveTo(0.5, 0.6, 0.9, 0.6, 0.9, 0.1);
        head.closePath();

        g2d.setColor(HEAD_COLOR);
        g2d.fill(head);

        g2d.translate(0.65, 0.68);
        g2d.rotate(-0.25);

        var translation = AffineTransform.getTranslateInstance(-0.28, 0.0);

        var eyeballs = new Area(new Ellipse2D.Double(0.015, -0.125, 0.25, 0.25));
        eyeballs.add(eyeballs.createTransformedArea(translation));

        var pupils = new Area(new Ellipse2D.Double(0.12, -0.08, 0.10, 0.10));
        pupils.add(pupils.createTransformedArea(translation));

        var eyelids = eyeballs.createTransformedArea(new AffineTransform());
        eyelids.subtract(eyelids.createTransformedArea(
            AffineTransform.getScaleInstance(1.0, blinkingFactor)));

        g2d.setColor(EYEBALL_COLOR);
        g2d.fill(eyeballs);

        g2d.setColor(PUPIL_COLOR);
        g2d.fill(pupils);

        g2d.setColor(HEAD_COLOR);
        g2d.fill(eyelids);
    }
}

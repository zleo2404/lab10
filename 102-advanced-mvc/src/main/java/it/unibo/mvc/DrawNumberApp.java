package it.unibo.mvc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 */
public final class DrawNumberApp implements DrawNumberViewObserver {
    private static final int MIN = 0;
    private static final int MAX = 100;
    private static final int ATTEMPTS = 10;

    private final DrawNumber model;
    private final List<DrawNumberView> views;

    /**
     * @param views
     *            the views to attach
     * @throws Exception 
     */
    public DrawNumberApp(final DrawNumberView... views) {
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final DrawNumberView view: views) {
            view.setObserver(this);
            view.start();
        }
        int min = MIN;
        int max = MAX;
        int attempts = ATTEMPTS;
        final Configuration.Builder builder = new Configuration.Builder();
        try (BufferedReader r = new BufferedReader(
            new InputStreamReader(
                ClassLoader.getSystemResourceAsStream("config.yml"), "UTF-8"))) {
                    String line = r.readLine();
                    while (line != null) {
                        final String[] diviso = line.split(":");
                        switch (diviso[0]) {
                            case "maximum":
                                max = Integer.parseInt(diviso[1].trim());
                                builder.setMax(max);
                                break;
                            case "minimum":
                                min = Integer.parseInt(diviso[1].trim());
                                builder.setMin(min);
                                break;
                            case "attempts":
                                attempts = Integer.parseInt(diviso[1].trim());
                                builder.setAttempts(attempts);
                                break;
                            default:
                                break;
                        }
                        line = r.readLine();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage()); // NOPMD
                }
        final Configuration build = builder.build();
        this.model = new DrawNumberImpl(build.getMin(), build.getMax(), build.getAttempts());
    }

    @Override
    public void newAttempt(final int n) {
        try {
            final DrawResult result = model.attempt(n);
            for (final DrawNumberView view: views) {
                view.result(result);
            }
        } catch (IllegalArgumentException e) {
            for (final DrawNumberView view: views) {
                view.numberIncorrect();
            }
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    @Override
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        //System.exit(0);
    }

    /**
     * @param args
     *            ignored
     * @throws FileNotFoundException 
     * @throws Exception 
     */
    public static void main(final String... args) throws FileNotFoundException  {
        new DrawNumberApp(
            new DrawNumberViewImpl(), 
            new DrawNumberViewImpl(), 
            new PrintStreamView(System.out), 
            new PrintStreamView("output.log")); 
    }

}

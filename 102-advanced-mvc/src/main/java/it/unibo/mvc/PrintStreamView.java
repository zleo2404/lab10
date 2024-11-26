/**
 * 
 */
package it.unibo.mvc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * This class implements a view that can write on any PrintStream.
 */
public final class PrintStreamView implements DrawNumberView {

    private final PrintStream out;

    /**
     * Builds a new PrintStreamView.
     *
     * @param stream the {@link PrintStream} where to write
     */
    public PrintStreamView(final PrintStream stream) {
        if (stream != null) {
            this.out = new PrintStream(stream, true, StandardCharsets.UTF_8); // Usando UTF-8 per garantire la codifica
        } else {
            throw new IllegalArgumentException("PrintStream cannot be null.");
        }
    }

    /**
     * Builds a {@link PrintStreamView} that writes on file, given a path.
     * 
     * @param path a file path
     * @throws FileNotFoundException 
     */
    public PrintStreamView(final String path) throws FileNotFoundException {
        this.out = new PrintStream(new FileOutputStream(new File(path)), true, StandardCharsets.UTF_8);
    }

    @Override
    public void setObserver(final DrawNumberViewObserver observer) {
        /*
         * This UI is output only.
         */
    }

    @Override
    public void start() {
        /*
         * PrintStreams are always ready.
         */
    }

    @Override
    public void numberIncorrect() {
        out.println("You must enter a number");
    }

    @Override
    public void result(final DrawResult res) {
        out.println(res.getDescription());
    }

    @Override
    public void displayError(final String message) {
        System.out.println(message); // NOPMD
    }

}

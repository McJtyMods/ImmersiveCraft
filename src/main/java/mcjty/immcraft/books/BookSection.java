package mcjty.immcraft.books;

import java.util.ArrayList;
import java.util.List;

public class BookSection {

    private final String name;
    private final List<BookElement> elements = new ArrayList<>();

    public BookSection(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addElement(BookElement element) {
        elements.add(element);
    }

    public List<BookElement> getElements() {
        return elements;
    }

    public int getWidth() {
        int maxwidth = 0;

        int curw = 0;
        for (BookElement element : elements) {
            if (element.getWidth() == -1) {
                curw = 0;
            } else {
                curw += element.getWidth();
                if (curw > maxwidth) {
                    maxwidth = curw;
                }
            }
        }

        return maxwidth;
    }

    public int getHeight() {
        int maxheight = 0;

        int curh = 0;
        for (BookElement element : elements) {
            curh = Math.max(curh, element.getHeight());
            if (element.getWidth() == -1) {
                maxheight += curh;
                curh = 0;
            }
        }

        return maxheight;
    }

    public static class Cursor {
        private final int maxwidth;

        private int curx = 0;
        private int cury = 0;
        private int maxh = 0;
        private int maxw = 0;

        public Cursor(int maxwidth) {
            this.maxwidth = maxwidth;
        }

        public int getX() {
            return curx;
        }

        public int getY() {
            return cury;
        }

        public int getMaxh() {
            return maxh;
        }

        public int getMaxw() {
            return maxw;
        }

        public void newline() {
            cury += maxh;
            maxh = 0;
            curx = 0;
        }

        public void add(int w, int h) {
            curx += w;
            if (curx > maxw) {
                maxw = curx;
            }
            if (h > maxh) {
                maxh = h;
            }
        }

        public boolean fits(int w) {
            return curx + w <= maxwidth;
        }

        public int remaining() {
            return maxwidth - curx;
        }

        public int max() {
            return maxwidth;
        }

        public void consolidate() {
            if (curx > 0) {
                newline();
            }
        }
    }

    public RenderSection renderAtWidth(int maxwidth) {
        RenderSection renderSection = new RenderSection();

        Cursor cursor = new Cursor(maxwidth);
        for (BookElement element : elements) {
            int w = element.getWidth();
            int h = element.getHeight();
            if (w == -1) {
                cursor.newline();
            } else if (cursor.fits(w)) {
                renderSection.addElement(element.createRenderElement(cursor.getX(), cursor.getY()));
                cursor.add(w, h);
            } else {
                List<BookElement> splitted = element.split(cursor.remaining(), cursor.max());
                for (BookElement s : splitted) {
                    w = s.getWidth();
                    h = s.getHeight();
                    if (w == -1) {
                        cursor.newline();
                    } else {
                        renderSection.addElement(s.createRenderElement(cursor.getX(), cursor.getY()));
                        cursor.add(w, h);
                    }
                }
            }
        }
        cursor.consolidate();;
        renderSection.setWidth(cursor.getMaxw());
        renderSection.setHeight(cursor.getY());
        return renderSection;
    }
}

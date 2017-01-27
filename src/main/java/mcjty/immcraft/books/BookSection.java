package mcjty.immcraft.books;

import java.util.ArrayList;
import java.util.List;

import static mcjty.immcraft.books.BookElement.*;

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
            if (element.getWidth() == WIDTH_NEWLINE) {
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
            if (element.getWidth() == WIDTH_NEWLINE) {
                maxheight += curh;
                curh = 0;
            } else if (element.getWidth() == WIDTH_FULLWIDTH) {
                maxheight += curh;
                curh = 0;
            } else if (element.getWidth() == WIDTH_NEWPARAGRAPH) {
                if (curh == 0) {
                    curh = element.getHeight();
                }
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
            if (w == WIDTH_NEWLINE) {
                cursor.newline();
            } else if (w == WIDTH_NEWPARAGRAPH) {
                if (cursor.getMaxh() == 0) {
                    cursor.add(1, element.getHeight());
                }
                cursor.newline();
            } else if (w == WIDTH_FULLWIDTH) {
                renderSection.addElement(element.createRenderElement(cursor.getX(), cursor.getY()));
                cursor.newline();
            } else if (cursor.fits(w)) {
                renderSection.addElement(element.createRenderElement(cursor.getX(), cursor.getY()));
                cursor.add(w, h);
            } else {
                cursor.newline();
                renderSection.addElement(element.createRenderElement(cursor.getX(), cursor.getY()));
                cursor.add(w, h);
            }
        }
        cursor.consolidate();
        renderSection.setWidth(cursor.getMaxw());
        renderSection.setHeight(cursor.getY());
        return renderSection;
    }
}

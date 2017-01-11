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

    public RenderSection renderAtWidth(int newwidth) {
        RenderSection resizedSection = new RenderSection();

        int curx = 0;
        int cury = 0;
        int maxh = 0;
        int maxw = 0;
        for (BookElement element : elements) {
            int w = element.getWidth();
            int h = element.getHeight();
            if (w == -1) {
                cury += maxh;
                maxh = 0;
                curx = 0;
            } else if (w <= curx + newwidth) {
                resizedSection.addElement(element.createRenderElement(curx, cury));
                if (h > maxh) {
                    maxh = h;
                }
                curx += element.getWidth();
            } else {
                BookElement[] splitted = element.split(newwidth - curx);
                if (splitted.length == 1) {
                    // Element cannot be split. Move to next line
                    curx = 0;
                    cury += maxh;
                    resizedSection.addElement(splitted[0].createRenderElement(curx, cury));
                    curx = splitted[0].getWidth();
                    maxh = splitted[0].getHeight();
                } else {
                    resizedSection.addElement(splitted[0].createRenderElement(curx, cury));
                    if (curx + w > maxw) {
                        maxw = curx + w;
                    }
                    cury += maxh;
                    curx = 0;
                    resizedSection.addElement(splitted[1].createRenderElement(curx, cury));
                    curx = splitted[1].getWidth();
                    maxh = splitted[1].getHeight();
                }
            }
            if (curx > maxw) {
                maxw = curx;
            }
        }
        resizedSection.setWidth(maxw);
        resizedSection.setHeight(cury);
        return resizedSection;
    }
}

package mcjty.immcraft.items;

public enum BookType {
    BOOK_RED("dummybook_red"),
    BOOK_BLUE("dummybook_blue"),
    BOOK_GREEN("dummybook_green"),
    BOOK_YELLOW("dummybook_yellow"),
    BOOK_SMALL_RED("dummybook_small_red"),
    BOOK_SMALL_BLUE("dummybook_small_blue"),
    BOOK_SMALL_GREEN("dummybook_small_green"),
    BOOK_SMALL_YELLOW("dummybook_small_yellow");

    private final String model;

    BookType(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}

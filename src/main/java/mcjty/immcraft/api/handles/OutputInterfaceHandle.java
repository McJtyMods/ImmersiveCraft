package mcjty.immcraft.api.handles;

public class OutputInterfaceHandle extends DefaultInterfaceHandle {

    public OutputInterfaceHandle() {
    }

    public OutputInterfaceHandle(String selectorID) {
        super(selectorID);
    }

    @Override
    public boolean isOutput() {
        return true;
    }
}

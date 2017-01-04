package mcjty.intwheel.api;

/**
 * Main interface for this mod. Use this to add custom support for the interaction wheel
 * Get a reference to an implementation of this interface by calling:
 *         FMLInterModComms.sendFunctionMessage("intwheel", "getTheWheel", "<whatever>.YourClass$GetTheWheel");
 */
public interface IInteractionWheel {

    /**
     * Optionally register a provider for your wheel support. You don't have to do this. You
     * can also implement IWheelBlockSupport in your block instead. If you register a provider
     * with the same string ID as one that already exists it will replace that provider. This
     * is one way to replace the standard provider. Interaction Wheel has the following standard
     * provider:
     *
     *   - "intwheel:default": the default provider
     *
     * @param provider
     */
    void registerProvider(IWheelActionProvider provider);

    /**
     * Get the wheel action registry where you can register your own actions
     * @return
     */
    IWheelActionRegistry getRegistry();
}

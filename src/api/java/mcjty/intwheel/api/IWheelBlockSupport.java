package mcjty.intwheel.api;

import java.util.Set;

/**
 * Implement this in your block to support custom wheel actions
 */
public interface IWheelBlockSupport {

    /**
     * Update the actions. You get a set of action elements that is already filled
     * in by all the registered providers. You can update the set here. Even remove
     * actions if you want.
     * @param actions a set with action ids that you can modify
     */
    void updateWheelActions(Set<String> actions);
}

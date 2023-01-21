package com.vpavlov.services.machine.api;

/**
 * Functional interface for title generation for machine nodes
 *
 * @author vpavlov
 */
@FunctionalInterface
public interface TitleGenerator {

    /**
     * Title generation method. Generates title for machine node with given index
     *
     * @param index the machine node index
     * @return generated title
     */
    String generateTitle(int index);
}

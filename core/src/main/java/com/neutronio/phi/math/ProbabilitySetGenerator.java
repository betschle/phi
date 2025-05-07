package com.neutronio.phi.math;

/**
 * Randomly choses an entry from a {@link ProbabilitySet}.
 */
public class ProbabilitySetGenerator {


    public ProbabilitySetGenerator() {
    }


    /**
     * Guarantees returning one single entry. Similar to throwing a dart towards a target and obtaining
     * the location of the dart.
     * <br>
     * <br>
     * Suppose we have a probability set with the following weights:
     * <pre>
     *     A = 50, B = 20, C = 20
     *     Sum = 90
     * </pre>
     *
     * Then for Random = 60 expected output = B
     * Visualization:
     * <pre>
     *            60
     *             v
     * [----A---][-B-][-C-]
     * 0        50   70   90
     * </pre>
     * @param generator the number generator to use
     * @param probabilitySet
     * @param <T>
     * @return
     */
    public <T> T generate(NumberGenerator generator, ProbabilitySet<T> probabilitySet) {
        float number = generator.getRandomFloat( probabilitySet.sum(), 0);
        return pickEntry(number, probabilitySet);
    }

    /**
     * Picks an entry based on a random number.
     * @param random expected range 0 to {@link ProbabilitySet#sum()}
     * @param probabilitySet the set to pick an entry from
     * @param <T>
     * @return null if random is somehow not within range of the probabilitySet
     */
    protected <T> T pickEntry(float random, ProbabilitySet<T> probabilitySet) {
        float min = 0;
        float max = 0;
        for( int i = 0; i < probabilitySet.size(); i++ ) {
            if( i == 0) {
                min = 0;
                max = probabilitySet.getProbabilityAt(0);
            } else {
                min += probabilitySet.getProbabilityAt(i - 1);
                max += probabilitySet.getProbabilityAt(i);
            }

            if( random >= min && random < max ) {
                return probabilitySet.getItemAt(i);
            }
        }
        return null;
    }
}

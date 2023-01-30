package com.hxy.algo.bool.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class LogicComposeConjunctionList implements MultiOrderlyConjunctionList {

    static {
        System.out.println("LogicComposeConjunctionList");
    }

    private static class ConjunctionHolder {
        private static final Comparator<ConjunctionHolder> COMPARATOR = Comparator.comparingInt(ConjunctionHolder::id);

        private final int posInContainer;
        private final int posInConjunctions;
        private final Conjunction conjunction;

        public ConjunctionHolder(int idxInContainer, int idxInConjunctionList, Conjunction conjunction) {
            this.posInContainer = idxInContainer;
            this.posInConjunctions = idxInConjunctionList;
            this.conjunction = conjunction;
        }

        public int id() {
            return conjunction.getId();
        }

        private static PriorityQueue<ConjunctionHolder> newPriorityQueue() {
            return new PriorityQueue<>(ConjunctionHolder.COMPARATOR);
        }
    }

    private List<List<Conjunction>> conjunctionsContainer = new ArrayList<>();
    private PriorityQueue<ConjunctionHolder> priorityQueue = ConjunctionHolder.newPriorityQueue();

    @Override
    public void addOrderlyConjunctionList(List<Conjunction> conjunctions) {
        conjunctionsContainer.add(conjunctions);
        ConjunctionHolder head = new ConjunctionHolder(
            conjunctionsContainer.size() - 1,
            0,
            conjunctions.get(0)
        );
        priorityQueue.add(head);
    }

    // id >= 0
    @Override
    public Conjunction first() {
        return nextGreaterThenOrEqualTo(new Conjunction(-1, Collections.emptyList()));
    }

    @Override
    public Conjunction nextGreaterThenOrEqualTo(Conjunction conjunction) {
        int id = conjunction.getId();
        ConjunctionHolder poll;
        while ((poll = priorityQueue.poll()) != null) {
            if (poll.id() >= id) {
                List<Conjunction> conjunctions = conjunctionsContainer.get(poll.posInContainer);
                int nextPosInConjunctions = poll.posInConjunctions + 1;
                if (nextPosInConjunctions < conjunctions.size()) {
                    ConjunctionHolder next = new ConjunctionHolder(
                        poll.posInContainer,
                        nextPosInConjunctions,
                        conjunctions.get(nextPosInConjunctions)
                    );
                    priorityQueue.add(next);
                }

                return poll.conjunction;
            } else {
                List<Conjunction> conjunctions = conjunctionsContainer.get(poll.posInContainer);
                Conjunction searchConjunction = new Conjunction(id, Collections.emptyList());
                int index = Collections.binarySearch(conjunctions, searchConjunction, Conjunction.COMPARATOR);
                if (index < 0) {
                    index = -index - 1;
                }
                if (index < conjunctions.size()) {
                    ConjunctionHolder next = new ConjunctionHolder(
                        poll.posInContainer,
                        index,
                        conjunctions.get(index)
                    );
                    priorityQueue.add(next);
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 3, 5, 7);
        System.out.println(Collections.binarySearch(list, 1));
        System.out.println(Collections.binarySearch(list, 7));
        System.out.println(Collections.binarySearch(list, 0));
        System.out.println(Collections.binarySearch(list, 8));
        System.out.println(Collections.binarySearch(list, 4));
    }

}

package de.laugenbreze.dungeonsalive.world.water;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/** Bounded round-robin selection over entity IDs; a crowd cannot permanently win by tick iteration order. */
public final class CurrentWorkQueue {
    public static final int BUDGET = 64;
    public static final int PRIORITY_RESERVE = 16;
    private int priorityCursor, ordinaryCursor;
    private final PriorityQueue<Integer> priority = new PriorityQueue<>(Comparator.comparingLong((Integer id) -> distance(id, priorityCursor)).reversed());
    private final PriorityQueue<Integer> ordinary = new PriorityQueue<>(Comparator.comparingLong((Integer id) -> distance(id, ordinaryCursor)).reversed());

    public void offer(int id, boolean controlled) {
        var queue = controlled ? priority : ordinary;
        (controlled ? ordinary : priority).remove(id);
        if (queue.contains(id)) return;
        queue.add(id);
        if (queue.size() > BUDGET) queue.poll();
    }
    public List<Integer> drain() {
        var controlled = priority.stream().sorted(Comparator.comparingLong(id -> distance(id, priorityCursor))).toList();
        var others = ordinary.stream().sorted(Comparator.comparingLong(id -> distance(id, ordinaryCursor))).toList();
        var selected = new ArrayList<Integer>(BUDGET);
        int reserved = Math.min(PRIORITY_RESERVE, controlled.size());
        int normal = Math.min(BUDGET - reserved, others.size());
        int extra = Math.min(BUDGET - reserved - normal, controlled.size() - reserved);
        // Players and piloted boats are applied first, with ordinary objects retaining a fair share under load.
        selected.addAll(controlled.subList(0, reserved + extra));
        selected.addAll(others.subList(0, normal));
        priority.clear(); ordinary.clear();
        if (reserved + extra > 0) priorityCursor = controlled.get(reserved + extra - 1);
        if (normal > 0) ordinaryCursor = others.get(normal - 1);
        return List.copyOf(selected);
    }
    public int queued() { return priority.size() + ordinary.size(); }
    private static long distance(int id, int cursor) {
        long value = (Integer.toUnsignedLong(id) - Integer.toUnsignedLong(cursor)) & 0xffff_ffffL;
        return value == 0 ? 0x1_0000_0000L : value;
    }
}

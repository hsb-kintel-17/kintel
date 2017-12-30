package de.kintel.ki;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ArrayDequeExplorativeTest {

    @Test
    public void addAllFromList() {
        final ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.push(4);

        List<Integer> someIntegers = Lists.newArrayList(1, 2, 3);
        deque.addAll(someIntegers);
        assertThat( deque.pollFirst(), is(4));
        assertThat( deque.pollFirst(), is(1));
        assertThat( deque.pollFirst(), is(2));
        assertThat( deque.pollFirst(), is(3));
    }

    @Test
    public void addAllFromDequeue() {
        final ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.push(4);

        Deque<Integer> someIntegers = new ArrayDeque<>();
        someIntegers.push(1);
        someIntegers.push(2);
        someIntegers.push(3);

        deque.addAll(someIntegers);
        assertThat( deque.pollFirst(), is(4));
        assertThat( deque.pollFirst(), is(3));
        assertThat( deque.pollFirst(), is(2));
        assertThat( deque.pollFirst(), is(1));
    }

}

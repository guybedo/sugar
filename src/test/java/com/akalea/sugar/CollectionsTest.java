package com.akalea.sugar;

import org.junit.Assert;
import org.junit.Test;
import static com.akalea.sugar.Collections.*;

import java.util.List;

public class CollectionsTest {

    @Test
    public void test() {
        List<Integer> integers = list(1, 2, 3, 4);
        Assert.assertEquals(List.of(1, 2, 3, 4), integers);

        Assert.assertEquals((Integer) 4, max(integers));
        Assert.assertEquals((Integer) 3, max(integers, i -> i % 4));
        Assert.assertEquals((Integer) 5, max(map(integers, i -> i + 1)));
        
        Assert.assertEquals((Integer) 1, min(integers));
        Assert.assertEquals((Integer) 4, min(integers, i -> i % 4));
        Assert.assertEquals((Integer) 2, min(map(integers, i -> i + 1)));
    }
}

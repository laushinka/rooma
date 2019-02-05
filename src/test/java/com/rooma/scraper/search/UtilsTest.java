package com.rooma.scraper.search;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class UtilsTest {
    @Test
    public void decodeStr() {
        String str = Utils.decodeStr("k\\u00f6penick");

        assertThat(str, is("köpenick"));
    }
}

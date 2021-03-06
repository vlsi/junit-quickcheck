/*
 The MIT License

 Copyright (c) 2010-2018 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@RunWith(JUnitQuickcheck.class)
public class Repro179 {
    public static class Other {
        private final Number test;

        Other(Number test) {
            this.test = test;
        }

        @Override public boolean equals(Object o) {
            return o instanceof Other && test.equals(((Other) o).test);
        }

        @Override public int hashCode() {
            return test.hashCode();
        }

        @Override public String toString() {
            return test.toString();
        }
    }

    public static class Others extends Generator<Other> {
        public Others() {
            super(Other.class);
        }

        @Override public Other generate(SourceOfRandomness r, GenerationStatus s) {
            return new Other(gen(r).type(Number.class).generate(r, s));
        }
    }

    @Property public void sameFuncTwice(
        Function<@From(Others.class) Other, @From(Others.class) Other> func,
        @From(Others.class) Other test) {

        assertThat(func.apply(test), is(func.apply(test)));
    }

    @Property public void sameBiFuncTwice(
        BiFunction<
            @From(Others.class) Other,
            @From(Others.class) Other,
            @From(Others.class) Other> func,
        @From(Others.class) Other first,
        @From(Others.class) Other second) {

        assertThat(func.apply(first, second), is(func.apply(first, second)));
    }
}

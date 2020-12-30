// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.base.function;

/**
 * Based on Java 8's java.util.function.Supplier.
 * Same as Callable<T>, but without a checked Exception.
 *
 * @param <T> Return type.
 */
public interface Supplier<T> {
    /**
     * Returns a value.
     */
    T get();
}

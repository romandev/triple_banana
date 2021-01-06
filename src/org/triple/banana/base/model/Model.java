// Copyright 2020 The Triple Banana Authors. All rights reserved.
// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at https://mozilla.org/MPL/2.0/.

package org.triple.banana.base.model;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.triple.banana.base.function.Supplier;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a generic model container class to define custom model data.
 *
 * How to define the custom model data:
 *   class UserDefinedModelData implements Model.Data {
 *       int hello = 1;
 *       ...
 *   }
 *
 * How to use that:
 *   // Create a new model with `UserDefinedModelData`
 *   Model<UserDefinedModelData> model = new Model<>(UserDefinedModelData::new);
 *
 *   // Add a listener to listen the model data changed event
 *   model.addListener(changedData -> {
 *       doSomething(changedData);
 *   });
 *
 *   // Update the model data (but it is actually not updated yet before commit())
 *   model.data.hello = 2;
 *
 *   // Swap the pending data with the internal data and then notify to listeners
 *   model.commit();
 */
public class Model<T extends Model.Data> {
    private static final @NonNull String TAG = "Model";
    private final @NonNull Supplier<T> mFactory;
    private final @NonNull Set<Listener<T>> mListeners;
    private @NonNull byte[] mInternalData;

    public final @NonNull T data;

    public Model(@NonNull Supplier<T> factory) {
        mFactory = factory;
        mListeners = new HashSet<>();
        mInternalData = new byte[0];
        data = factory.get();
    }

    public interface Data extends Serializable {}

    public interface Listener<T> {
        void onUpdate(@NonNull T data);
    }

    public void addListener(@NonNull Listener<T> listener) {
        mListeners.add(listener);
    }

    public void removeListener(@Nullable Listener<T> listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public void commit() {
        byte[] serializedData = serialize(this.data);
        if (!wasDataChanged(mInternalData, serializedData)) return;

        mInternalData = serializedData;
        for (Listener<T> listener : mListeners) {
            listener.onUpdate(deserialize(mInternalData));
        }
    }

    private boolean wasDataChanged(@NonNull byte[] oldData, @NonNull byte[] newData) {
        if (oldData.length == 0) return true;
        return !Arrays.equals(oldData, newData);
    }

    private @NonNull byte[] serialize(@NonNull T data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(data);
            byte[] serializedData = baos.toByteArray();
            if (serializedData != null) return serializedData;
        } catch (Exception e) {
            Log.e(TAG, "serialize(): " + e.toString());
        }
        return new byte[0];
    }

    private @NonNull T deserialize(@NonNull byte[] serializedData) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
                ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (Exception e) {
            Log.e(TAG, "deserialize(): " + e.toString());
        }
        return mFactory.get();
    }
}

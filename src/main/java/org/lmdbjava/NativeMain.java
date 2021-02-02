/*-
 * #%L
 * LmdbJavaNative
 * %%
 * Copyright (C) 2016 - 2021 The LmdbJava Open Source Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.lmdbjava;

import com.oracle.svm.core.c.CGlobalData;
import com.oracle.svm.core.c.CGlobalDataFactory;
import com.oracle.svm.core.c.function.CEntryPointOptions;
import com.oracle.svm.core.c.function.CEntryPointSetup;
import org.graalvm.nativeimage.PinnedObject;
import org.graalvm.nativeimage.StackValue;
import org.graalvm.nativeimage.c.CContext;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.function.CEntryPointLiteral;
import org.graalvm.nativeimage.c.type.CFloatPointer;
import org.graalvm.nativeimage.c.type.CIntPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;
import org.graalvm.nativeimage.c.type.CTypeConversion.CCharPointerHolder;
import org.graalvm.nativeimage.c.type.WordPointer;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.word.Pointer;

import java.io.File;

/**
 * Entry-point to the application
 */
@CContext(LMDB.Directives.class)
public class NativeMain {

    static CGlobalData<WordPointer> env =
        CGlobalDataFactory.createBytes(() -> 8);

    static String msg(int rc) {
        return CTypeConversion.toJavaString(LMDB.mdb_strerror(rc));
    }

    public static void main(String[] args) {

        String filePath = "/tmp/nativetestdb";

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        int rc;

        rc = LMDB.mdb_env_create(env.get().addressOf(0));
        System.out.println("create: " + msg(rc));

        try (CCharPointerHolder path = CTypeConversion.toCString(filePath)) {
            rc = LMDB.mdb_env_open(env.get().read(), path.get(), 0, 0664);
            System.out.println("open: " + msg(rc));
        }



    }

}

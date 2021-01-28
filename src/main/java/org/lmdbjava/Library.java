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

import static java.io.File.createTempFile;
import static java.lang.Boolean.getBoolean;
import static java.lang.System.getProperty;
import static java.lang.Thread.currentThread;
import static java.util.Locale.ENGLISH;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static jnr.ffi.LibraryLoader.create;
import static jnr.ffi.Runtime.getRuntime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

//import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jnr.ffi.Pointer;
import jnr.ffi.Struct;
import jnr.ffi.annotations.Delegate;
import jnr.ffi.annotations.In;

/**
 * JNR-FFI interface to LMDB.
 *
 * <p>
 * For performance reasons pointers are used rather than structs.
 */
public final class Library {

  /**
   * Java system property name that can be set to disable automatic extraction
   * of the LMDB system library from the LmdbJava JAR. This may be desirable if
   * an operating system-provided LMDB system library is preferred (eg operating
   * system package management, vendor support, special compiler flags, security
   * auditing, profile guided optimization builds, faster startup time by
   * avoiding the library copy etc).
   */
  public static final String DISABLE_EXTRACT_PROP = "lmdbjava.disable.extract";
  /**
   * Java system property name that can be set to the path of an existing
   * directory into which the LMDB system library will be extracted from the
   * LmdbJava JAR. If unspecified the LMDB system library is extracted to the
   * <code>java.io.tmpdir</code>. Ignored if the LMDB system library is not
   * being extracted from the LmdbJava JAR (as would be the case if other
   * system properties defined in <code>Library</code> have been set).
   */
  public static final String LMDB_EXTRACT_DIR_PROP = "lmdbjava.extract.dir";
  /**
   * Java system property name that can be set to provide a custom path to a
   * external LMDB system library. If set, the system property
   * DISABLE_EXTRACT_PROP will be overridden.
   */
  public static final String LMDB_NATIVE_LIB_PROP = "lmdbjava.native.lib";
  /**
   * Indicates whether automatic extraction of the LMDB system library is
   * permitted.
   */
  public static final boolean SHOULD_EXTRACT = !getBoolean(DISABLE_EXTRACT_PROP);
  /**
   * Indicates the directory where the LMDB system library will be extracted.
   */
  static final String EXTRACT_DIR = getProperty(LMDB_EXTRACT_DIR_PROP,
                                                getProperty("java.io.tmpdir"));
  static final Lmdb LIB;
  static final jnr.ffi.Runtime RUNTIME;
  /**
   * Indicates whether external LMDB system library is provided.
   */
  static final boolean SHOULD_USE_LIB = nonNull(
      getProperty(LMDB_NATIVE_LIB_PROP));
  private static final String LIB_NAME = "lmdb";

  static {
    final String libToLoad;

    final String arch = getProperty("os.arch");
    final boolean arch64 = "x64".equals(arch) || "amd64".equals(arch)
                               || "x86_64".equals(arch);

    final String os = getProperty("os.name");
    final boolean linux = os.toLowerCase(ENGLISH).startsWith("linux");
    final boolean osx = os.startsWith("Mac OS X");
    final boolean windows = os.startsWith("Windows");

    if (SHOULD_USE_LIB) {
      libToLoad = getProperty(LMDB_NATIVE_LIB_PROP);
    } else if (SHOULD_EXTRACT && arch64 && linux) {
      libToLoad = extract("org/lmdbjava/lmdbjava-native-linux-x86_64.so");
    } else if (SHOULD_EXTRACT && arch64 && osx) {
      libToLoad = extract("org/lmdbjava/lmdbjava-native-osx-x86_64.dylib");
    } else if (SHOULD_EXTRACT && arch64 && windows) {
      libToLoad = extract("org/lmdbjava/lmdbjava-native-windows-x86_64.dll");
    } else {
      libToLoad = LIB_NAME;
    }

    LIB = create(Lmdb.class).load(libToLoad);
    RUNTIME = getRuntime(LIB);
  }

  private Library() {
  }

//  @SuppressFBWarnings("OBL_UNSATISFIED_OBLIGATION") // Spotbugs issue #432
  private static String extract(final String name) {
    final String suffix = name.substring(name.lastIndexOf('.'));
    final File file;
    try {
      final File dir = new File(EXTRACT_DIR);
      if (!dir.exists() || !dir.isDirectory()) {
        throw new IllegalStateException("Invalid extraction directory " + dir);
      }
      file = createTempFile("lmdbjava-native-library-", suffix, dir);
      file.deleteOnExit();
      final ClassLoader cl = currentThread().getContextClassLoader();
      try (InputStream in = cl.getResourceAsStream(name);
           OutputStream out = Files.newOutputStream(file.toPath())) {
        requireNonNull(in, "Classpath resource not found");
        int bytes;
        final byte[] buffer = new byte[4_096];
        while (-1 != (bytes = in.read(buffer))) {
          out.write(buffer, 0, bytes);
        }
      }
      return file.getAbsolutePath();
    } catch (final IOException e) {
      throw new LmdbException("Failed to extract " + name, e);
    }
  }

  /**
   * Structure to wrap a native <code>MDB_envinfo</code>. Not for external use.
   */
  @SuppressWarnings({"checkstyle:TypeName", "checkstyle:VisibilityModifier",
                     "checkstyle:MemberName"})
  public static final class MDB_envinfo extends Struct {

    public final Pointer f0_me_mapaddr;
    public final size_t f1_me_mapsize;
    public final size_t f2_me_last_pgno;
    public final size_t f3_me_last_txnid;
    public final u_int32_t f4_me_maxreaders;
    public final u_int32_t f5_me_numreaders;

    MDB_envinfo(final jnr.ffi.Runtime runtime) {
      super(runtime);
      this.f0_me_mapaddr = new Pointer();
      this.f1_me_mapsize = new size_t();
      this.f2_me_last_pgno = new size_t();
      this.f3_me_last_txnid = new size_t();
      this.f4_me_maxreaders = new u_int32_t();
      this.f5_me_numreaders = new u_int32_t();
    }
  }

  /**
   * Structure to wrap a native <code>MDB_stat</code>. Not for external use.
   */
  @SuppressWarnings({"checkstyle:TypeName", "checkstyle:VisibilityModifier",
                     "checkstyle:MemberName"})
  public static final class MDB_stat extends Struct {

    public final u_int32_t f0_ms_psize;
    public final u_int32_t f1_ms_depth;
    public final size_t f2_ms_branch_pages;
    public final size_t f3_ms_leaf_pages;
    public final size_t f4_ms_overflow_pages;
    public final size_t f5_ms_entries;

    MDB_stat(final jnr.ffi.Runtime runtime) {
      super(runtime);
      this.f0_ms_psize = new u_int32_t();
      this.f1_ms_depth = new u_int32_t();
      this.f2_ms_branch_pages = new size_t();
      this.f3_ms_leaf_pages = new size_t();
      this.f4_ms_overflow_pages = new size_t();
      this.f5_ms_entries = new size_t();
    }
  }

  /**
   * Custom comparator callback used by <code>mdb_set_compare</code>.
   */
  public interface ComparatorCallback {

    @Delegate
    int compare(@In Pointer keyA, @In Pointer keyB);

  }
}

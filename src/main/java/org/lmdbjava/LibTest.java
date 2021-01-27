package org.lmdbjava;

import static org.lmdbjava.Library.LIB;

import java.util.Objects;

import com.kenai.jffi.JffiTest;
import com.kenai.jffi.NativeType;

import jnr.ffi.byref.PointerByReference;
import jnr.ffi.provider.jffi.Provider;
//import static com.kenai.jffi.NativeType.POINTER;

public class LibTest {
	private static PointerByReference pr;
	private static jnr.ffi.Runtime r;
	private static Library lib;
	static Provider p;
	static com.kenai.jffi.NativeType np = NativeType.POINTER;
//	  static {
//		    final String libToLoad;
//
//		    final String arch = getProperty("os.arch");
//		    final boolean arch64 = "x64".equals(arch) || "amd64".equals(arch)
//		                               || "x86_64".equals(arch);
//
//		    final String os = getProperty("os.name");
//		    final boolean linux = os.toLowerCase(ENGLISH).startsWith("linux");
//		    final boolean osx = os.startsWith("Mac OS X");
//		    final boolean windows = os.startsWith("Windows");
//
//
//		    LIB = create(Lmdb.class).load("org/lmdbjava/lmdbjava-native-linux-x86_64.so");
////		    RUNTIME = getRuntime(LIB);
//		  }
	  
	public static void check() {
    	System.setProperty("java.library.path", "/home/paul/dev/graalvm/lmdb-viewer-maven/target/jni/x86_64-Linux");
    	System.out.printf("java.library.path=%s%n", System.getProperty("java.library.path"));
    	//libjffi-1.2.so
//		System.loadLibrary("libjffi-1.2.so");
		
		JffiTest.lookupTypeInfo(np);
		
		System.out.println("After lookup");
		Objects.requireNonNull(LIB);
	}
	

}

/**
 * 
 */
package com.kenai.jffi;

import com.kenai.jffi.Type.TypeInfo;

/**
 * @author paul
 *
 */
public class JffiTest {
	 private static TypeInfo typeInfo;
	 
    public static TypeInfo lookupTypeInfo(final NativeType nativeType) {
        try {
            final Foreign foreign = Foreign.getInstance();
            final long handle = foreign.lookupBuiltinType(nativeType.ffiType);
            if (handle == 0L) {
                throw new NullPointerException("invalid handle for native type " + nativeType);
            }

            return typeInfo = new TypeInfo(handle, foreign.getTypeType(handle), foreign.getTypeSize(handle), foreign.getTypeAlign(handle));

        } catch (final Throwable error) {
            throw new UnsatisfiedLinkError(
                    "could not get native definition for type `" + nativeType
                    + "`, original error message follows: " + error.getLocalizedMessage());
        }
    }
}

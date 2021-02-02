# LMDB viewer for GraalVM native image

This is a combination of two projects:
* lmdbjavanative - https://github.com/juji-io/lmdbjavanative
* lmdb-viewer - https://github.com/cleve/lmdb-viewer

Why do this this? 
I wanted to use lmdbjavanative as a dependency from lmdb-viewer in order to create a native image.
However, GraalVM does not support dynamic linking so I have simply copied lmdbjavanative source into lmdb-viewer.
This does not work, perhaps because lmdbjavanative wants to be built with JDK 8 rather than 11.

# lmdb-viewer
GUI to navigate over LMDB data

![alt text](https://raw.githubusercontent.com/cleve/lmdb-viewer/master/media/lmdb.png "Main window")

## Limitations

* Only single database files can be opened.
* Tested on MXLinux 19 (derived from Debian 10)
* Does not work - cannot open LMDB files!

### All platform

Requirements:

+ Java 11 GraalVM 21.0.0 (use sdkman)
+ Gluon

        https://gluonhq.com/products/javafx/

To build:
```
mvn clean client:build
```

## Executing the App
The JavaFX part seems to be fine, but cannot open an LMBD file:

```sh
Exception in thread "JavaFX Application Thread" java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
    at javafx.fxml.FXMLLoader$MethodHandler.invoke(FXMLLoader.java:1862)
    at javafx.fxml.FXMLLoader$ControllerMethodEventHandler.handle(FXMLLoader.java:1729)
    at com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:86)
    at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:234)
    at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:191)
    at com.sun.javafx.event.CompositeEventDispatcher.dispatchBubblingEvent(CompositeEventDispatcher.java:59)
    at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58)
    at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
    at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
    at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
    at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
    at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
    at com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74)
    at com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:49)
    at javafx.event.Event.fireEvent(Event.java:198)
    at javafx.scene.Node.fireEvent(Node.java:8889)
    at javafx.scene.control.Button.fire(Button.java:203)
    at com.sun.javafx.scene.control.behavior.ButtonBehavior.mouseReleased(ButtonBehavior.java:208)
    at com.sun.javafx.scene.control.inputmap.InputMap.handle(InputMap.java:274)
    at com.sun.javafx.event.CompositeEventHandler$NormalEventHandlerRecord.handleBubblingEvent(CompositeEventHandler.java:247)
    at com.sun.javafx.event.CompositeEventHandler.dispatchBubblingEvent(CompositeEventHandler.java:80)
    at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:234)
    at com.sun.javafx.event.EventHandlerManager.dispatchBubblingEvent(EventHandlerManager.java:191)
    at com.sun.javafx.event.CompositeEventDispatcher.dispatchBubblingEvent(CompositeEventDispatcher.java:59)
    at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:58)
    at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
    at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
    at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
    at com.sun.javafx.event.BasicEventDispatcher.dispatchEvent(BasicEventDispatcher.java:56)
    at com.sun.javafx.event.EventDispatchChainImpl.dispatchEvent(EventDispatchChainImpl.java:114)
    at com.sun.javafx.event.EventUtil.fireEventImpl(EventUtil.java:74)
    at com.sun.javafx.event.EventUtil.fireEvent(EventUtil.java:54)
    at javafx.event.Event.fireEvent(Event.java:198)
    at javafx.scene.Scene$MouseHandler.process(Scene.java:3856)
    at javafx.scene.Scene.processMouseEvent(Scene.java:1851)
    at javafx.scene.Scene$ScenePeerListener.mouseEvent(Scene.java:2584)
    at com.sun.javafx.tk.quantum.GlassViewEventHandler$MouseEventNotification.run(GlassViewEventHandler.java:409)
    at com.sun.javafx.tk.quantum.GlassViewEventHandler$MouseEventNotification.run(GlassViewEventHandler.java:299)
    at java.security.AccessController.doPrivileged(AccessController.java:102)
    at com.sun.javafx.tk.quantum.GlassViewEventHandler.lambda$handleMouseEvent$2(GlassViewEventHandler.java:447)
    at com.sun.javafx.tk.quantum.QuantumToolkit.runWithoutRenderLock(QuantumToolkit.java:412)
    at com.sun.javafx.tk.quantum.GlassViewEventHandler.handleMouseEvent(GlassViewEventHandler.java:446)
    at com.sun.glass.ui.View.handleMouseEvent(View.java:556)
    at com.sun.glass.ui.View.notifyMouse(View.java:942)
    at com.oracle.svm.jni.JNIJavaCallWrappers.jniInvoke_VA_LIST:Lcom_sun_glass_ui_View_2_0002enotifyMouse_00028IIIIIIIZZ_00029V(JNIJavaCallWrappers.java:0)
    at com.sun.glass.ui.gtk.GtkApplication._runLoop(GtkApplication.java)
    at com.sun.glass.ui.gtk.GtkApplication.lambda$runLoop$11(GtkApplication.java:277)
    at java.lang.Thread.run(Thread.java:834)
    at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:519)
    at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192)
Caused by: java.lang.reflect.InvocationTargetException
    at java.lang.reflect.Method.invoke(Method.java:566)
    at com.sun.javafx.reflect.Trampoline.invoke(MethodUtil.java:76)
    at java.lang.reflect.Method.invoke(Method.java:566)
    at com.sun.javafx.reflect.MethodUtil.invoke(MethodUtil.java:273)
    at com.sun.javafx.fxml.MethodHelper.invoke(MethodHelper.java:83)
    at javafx.fxml.FXMLLoader$MethodHandler.invoke(FXMLLoader.java:1857)
    ... 49 more
Caused by: java.lang.UnsatisfiedLinkError: could not load FFI provider jnr.ffi.provider.jffi.Provider
    at jnr.ffi.provider.InvalidProvider$1.loadLibrary(InvalidProvider.java:48)
    at jnr.ffi.LibraryLoader.load(LibraryLoader.java:392)
    at jnr.ffi.LibraryLoader.load(LibraryLoader.java:371)
    at org.lmdbjava.Library.<clinit>(Library.java:125)
    at com.oracle.svm.core.classinitialization.ClassInitializationInfo.invokeClassInitializer(ClassInitializationInfo.java:375)
    at com.oracle.svm.core.classinitialization.ClassInitializationInfo.initialize(ClassInitializationInfo.java:295)
    at org.lmdbjava.Env$Builder.open(Env.java:486)
    at org.lmdbjava.Env$Builder.open(Env.java:512)
    at core.DataBase.<init>(DataBase.java:27)
    at sample.Controller.openDatabase(Controller.java:68)
    ... 55 more
Caused by: java.lang.UnsatisfiedLinkError: could not get native definition for type `POINTER`, original error message follows: null
    at com.kenai.jffi.Type$Builtin.lookupTypeInfo(Type.java:253)
    at com.kenai.jffi.Type$Builtin.getTypeInfo(Type.java:237)
    at com.kenai.jffi.Type.resolveSize(Type.java:155)
    at com.kenai.jffi.Type.size(Type.java:138)
    at jnr.ffi.provider.jffi.NativeRuntime$TypeDelegate.size(NativeRuntime.java:178)
    at jnr.ffi.provider.AbstractRuntime.<init>(AbstractRuntime.java:48)
    at jnr.ffi.provider.jffi.NativeRuntime.<init>(NativeRuntime.java:57)
    at jnr.ffi.provider.jffi.NativeRuntime.<init>(NativeRuntime.java:41)
    at jnr.ffi.provider.jffi.NativeRuntime$SingletonHolder.<clinit>(NativeRuntime.java:53)
    at com.oracle.svm.core.classinitialization.ClassInitializationInfo.invokeClassInitializer(ClassInitializationInfo.java:375)
    at com.oracle.svm.core.classinitialization.ClassInitializationInfo.initialize(ClassInitializationInfo.java:295)
    at jnr.ffi.provider.jffi.NativeRuntime.getInstance(NativeRuntime.java:49)
    at jnr.ffi.provider.jffi.Provider.<init>(Provider.java:29)
    at java.lang.reflect.Constructor.newInstance(Constructor.java:490)
    at java.lang.Class.newInstance(DynamicHub.java:888)
    at jnr.ffi.provider.FFIProvider$SystemProviderSingletonHolder.getInstance(FFIProvider.java:68)
    at jnr.ffi.provider.FFIProvider$SystemProviderSingletonHolder.<clinit>(FFIProvider.java:57)
    at com.oracle.svm.core.classinitialization.ClassInitializationInfo.invokeClassInitializer(ClassInitializationInfo.java:375)
    at com.oracle.svm.core.classinitialization.ClassInitializationInfo.initialize(ClassInitializationInfo.java:295)
    at jnr.ffi.provider.FFIProvider.getSystemProvider(FFIProvider.java:35)
    at jnr.ffi.LibraryLoader.create(LibraryLoader.java:74)
    ... 62 more

``` 


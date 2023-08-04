# k8
A CHIP-8 emulator written in Kotlin.
The emulator aims to run with both an android and a JavaFX frontend allowing for portability.

The Kotlin patterns and code organisation is quite terrible from the perspective of present me (August 2023 me). 
Nonetheless, this is a sign of growth - I will let the original stay here as a historical artifact. 

## Code Organisation

The code organisation is as follows :

1. **`src/`** is the source code of the top level module. This contains common interfaces and the entire code for the CPU and the memory manager and other components that do not require any graphical support. 
2. **`k8-javafx`** depends on the top level module and contains code for the JavaFX based graphic implementation. The code for windowing and the _concrete_ drawing of the sprites is handled here. This provides an actual frame buffer for the Chip 8.

## Why Coroutines?

To recall my reasoning at the time; my belief was that having a separate coroutine for the drawing of the graphics and for the CPU computations would speed up the process. 

Of course, it did not; it just led to the misery known as "synchronisation hell". I had to rely on all sorts of weird hacks for message passing between threads, I did not use any shared mutable state proper and relied on a few atomic variables to do the message passing. 

I'm not sure why I did this - either coroutines did not have channels or I did not of their existence or something else.

In the meantime, I have realised that actual computation is nearly always cheaper than effectful code since effects rely on syscalls. Syscalls are expensive due to context switches. 
Nonetheless, Chip 8 does not have any particularly heavy graphical machinery and a single threaded architecture would have worked just fine. 

## How would I do it now?

This was probably my first non-trivial project. I lacked any experience in theoretical computer science and could not have relied on mathematics to guide the design process.

However, I do now. The Chip 8 is just a glorified state machine. Furthermore, all the transitions are history insensitive; so that when the transition $\sigma \rightarrow \sigma'$ happens the state $\sigma$ can be discarded. This is a classical use case for the interior mutability pattern.

We get the best of both worlds - mathematical clarity and performance.